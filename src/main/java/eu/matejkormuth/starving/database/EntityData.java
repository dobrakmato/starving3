/**
 * Starving - Bukkit API server mod with Zombies.
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.matejkormuth.starving.database;

import eu.matejkormuth.starving.database.annotations.AutoRegisterColumns;
import eu.matejkormuth.starving.database.annotations.PerformsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EntityData {

    private static final Logger log = LoggerFactory.getLogger(EntityData.class);

    private final Class<? extends Entity> clazz;
    private Constructor<? extends Entity> ctr;
    private List<Field> columns;
    private String tableName;
    private String uniqueIdName;
    private String setStatementPart;

    private Map<Long, Entity> cache = new HashMap<>();
    private PreparedStatement selectById;
    private PreparedStatement selectAll;
    private PreparedStatement updateById;

    private EntityData(Class<? extends Entity> clazz) {
        this.clazz = clazz;
        log.info("Creating EntityData for {}...", clazz.getSimpleName());
        this.findConstructor();
        this.findTableName();
        this.findColumns();
        this.findUniqueIDName();
        this.findSetStatementPart();
    }

    /**
     * Highly magic method for performing the darkest and worst coding practices. We
     * use this because it's easier to call create() then new EntityData(class).
     *
     * @return the RIGHT entity data
     */
    public static EntityData create() {
        try {
            return new EntityData(cast(Class.forName(new Exception().getStackTrace()[1].getClassName())));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Something is terribly wrong!");
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Entity> cast(Class<?> aClass) {
        return (Class<? extends Entity>) aClass;
    }

    @PerformsQuery(PerformsQuery.When.ALWAYS)
    public void findAll() {
        try {
            ResultSet resultSet = this.selectAll.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(this.getUniqueIdName());
                Entity e = this.instantiate(resultSet);
                if (e != null) {
                    this.cache.put(id, e);
                } else {
                    log.warn("Cannot cache entity of {} with ID {}!", this.getEntityClass().getName(), id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PerformsQuery(PerformsQuery.When.SOMETIMES)
    public Optional<Entity> findOne(long id) {

        if (this.cache.containsKey(id)) {
            return Optional.of(this.cache.get(id));
        } else {
            try {
                // We can set this here and don't need to create dynamic reflection method.
                this.selectById.setLong(0, id);

                ResultSet resultSet = this.selectById.executeQuery();
                if (resultSet.next()) {
                    Entity e = this.instantiate(resultSet);
                    if (e != null) {
                        this.cache.put(id, e);
                    } else {
                        log.warn("Cannot cache entity of {} with ID {}!", this.getEntityClass().getName(), id);
                    }
                    return Optional.ofNullable(e);
                } else {
                    return Optional.empty();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void initStatements(Connection connection) throws SQLException {
        selectAll = connection.prepareStatement("SELECT * FROM " + getTableName());
        selectById = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE " + getUniqueIdName() + " = ?");
        updateById = connection.prepareStatement("UPDATE " + getTableName() + " SET " + getSetStatementPart() + " WHERE " + getUniqueIdName() + " = ?");
    }

    private void findConstructor() {
        try {
            this.ctr = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class " + clazz.getName() + " has no no-arg public constructor!");
        }
    }

    private void findSetStatementPart() {
        StringBuilder sb = new StringBuilder();
        sb.append("SET ");
        for (int i = 0; i < columns.size(); i++) {
            sb
                    .append('`')
                    .append(columns.get(i).getName())
                    .append('`')
                    .append(" = ?");
            if (i != columns.size() - 1) {
                sb.append(", ");
            }
        }
        this.setStatementPart = sb.toString();
    }

    private void findColumns() {
        this.columns = new ArrayList<>();

        if (this.clazz.isAnnotationPresent(AutoRegisterColumns.class)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && Modifier.isPrivate(field.getModifiers())) {
                    this.columns.add(field);

                    // Check access permissions.
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                }
            }
        } else {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    // Check for unsupported feature.
                    if (!field.getAnnotation(Column.class).name().equals(field.getName())) {
                        throw new UnsupportedOperationException("Field name must be same as column name! ("
                                + field.getName() + ", " + field.getAnnotation(Column.class).name() + ")");
                    }

                    this.columns.add(field);

                    // Check access permissions.
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                }
            }
        }
    }

    private void findUniqueIDName() {
        for (Field field : columns) {
            if (field.getAnnotation(Column.class).unique()) {
                this.uniqueIdName = field.getName();
                return;
            }
        }
    }

    private void findTableName() {
        if (clazz.isAnnotationPresent(Table.class)) {
            if (clazz.getAnnotation(Table.class).name().equals("not_implemented")) {
                throw new RuntimeException("Can't find table name on " + clazz.getName() + "!");
            }
            this.tableName = clazz.getAnnotation(Table.class).name();
        } else {
            // TODO: Replace with better name creation algorithm
            String clazzName = clazz.getSimpleName().toLowerCase(Locale.getDefault());
            this.tableName = clazzName + "s";
        }
    }

    private String getTableName() {
        return tableName;
    }

    private String getUniqueIdName() {
        return uniqueIdName;
    }

    private String getSetStatementPart() {
        return setStatementPart;
    }

    private Entity instantiate(ResultSet resultSet) {
        Entity entity;
        try {
            entity = this.ctr.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Can't create instance of " + clazz.getName() + "!", e);
            return null;
        }
        for (Field field : columns) {
            try {
                field.set(entity, resultSet.getObject(field.getName(), field.getType()));
            } catch (IllegalAccessException | SQLException e) {
                log.error("Can't set value of " + clazz + " model!", e);
            }
        }
        return entity;
    }

    Class<?> getEntityClass() {
        return clazz;
    }

}

package com.pricegsm.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.*;

public class EntityMetadata {

    private String name;

    private Map<String, Column> columns = new HashMap<>();

    public static EntityMetadata from(Class type) {
        String entityName = StringUtils.uncapitalize(type.getSimpleName());

        EntityMetadata entityMetadata = new EntityMetadata();
        entityMetadata.name = entityName;

        PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(type);

        for (PropertyDescriptor property : properties) {
            //if property has read/write methods
            if (Utils.isAccessible(property)) {
                EntityMetadata.Column column = EntityMetadata.Column.from(property, entityName);
                entityMetadata.getColumns().put(column.getId(), column);
            }
        }

        return entityMetadata;
    }


    public String getName() {
        return name;
    }

    public Map<String, Column> getColumns() {
        return columns;
    }

    public static class Column {

        private String id;
        private String field;
        private String code;
        private List<ValidationRule> validations = new ArrayList<>();

        private static Column from(PropertyDescriptor propertyDescriptor, String entityName) {
            Column column = new Column();
            String propertyName = propertyDescriptor.getName();

            column.id = propertyName;
            column.field = propertyName;
            column.code = entityName + "." + column.field;

            for (Annotation a : propertyDescriptor.getReadMethod().getAnnotations()) {
                ValidationRule v = validationForAngular(a);

                if (v != null) {
                    column.getValidations().add(v);
                }
            }


            return column;
        }

        public String getId() {
            return id;
        }

        public String getField() {
            return field;
        }

        public String getCode() {
            return code;
        }

        public List<ValidationRule> getValidations() {
            return validations;
        }

        private static ValidationRule validationForAngular(Annotation annotation) {

            if (annotation instanceof NotNull) {
                NotNull a = (NotNull) annotation;
                return ValidationRule.create(a.message(), "Required", new Pair("required", ""));
            }
            if (annotation instanceof NotBlank) {
                NotBlank a = (NotBlank) annotation;
                return ValidationRule.create(a.message(), "Required", new Pair("required", ""));
            }
            if (annotation instanceof NotEmpty) {
                NotEmpty a = (NotEmpty) annotation;
                return ValidationRule.create(a.message(), "Required", new Pair("required", ""));
            }

            if (annotation instanceof Size) {
                Size a = (Size) annotation;
                Map<String, Object> keys = new LinkedHashMap<>();

                if (a.min() > 0) {
                    keys.put("minlength", a.min());
                }
                if (a.max() < Integer.MAX_VALUE) {
                    keys.put("maxlength", a.max());
                }

                return ValidationRule
                        .create(a.message(), "Size", keys)
                        .addArgs("min", a.min())
                        .addArgs("max", a.max());

            }

            if (annotation instanceof Length) {
                Length a = (Length) annotation;
                Map<String, Object> keys = new LinkedHashMap<>();

                if (a.min() > 0) {
                    keys.put("minlength", a.min());
                }
                if (a.max() < Integer.MAX_VALUE) {
                    keys.put("maxlength", a.max());
                }

                return ValidationRule
                        .create(a.message(), "Length", keys)
                        .addArgs("min", a.min())
                        .addArgs("max", a.max());
            }

            if (annotation instanceof Max) {
                Max a = (Max) annotation;

                return ValidationRule
                        .create(a.message(), "Max", new Pair("max", a.value()))
                        .addArgs("value", a.value());

            }
            if (annotation instanceof Min) {
                Min a = (Min) annotation;

                return ValidationRule
                        .create(a.message(), "Min", new Pair("min", a.value()))
                        .addArgs("value", a.value());

            }
            if (annotation instanceof Pattern) {
                Pattern a = (Pattern) annotation;

                return ValidationRule
                        .create(a.message(), "Pattern", new Pair("pattern", a.regexp()))
                        .addArgs("regexp", a.regexp());
            }

            return null;
        }

        public static class ValidationRule {
            private Map<String, Object> keys = new LinkedHashMap<>();
            private String name;
            private String message;
            private Map<String, Object> args = new HashMap<>();

            public static ValidationRule create(String message, String name, Pair... keys) {
                return new ValidationRule(message, name, keys);
            }

            public static ValidationRule create(String message, String name, Map<String, Object> keys) {
                return new ValidationRule(message, name, keys);
            }

            public ValidationRule(String message, String name, Pair... keys) {
                this.message = Utils.adaptValidationMessage(message);
                this.name = name;
                addKeys(keys);
            }

            public ValidationRule(String message, String name, Map<String, Object> keys) {
                this.message = Utils.adaptValidationMessage(message);
                this.name = name;
                this.keys = keys;
            }

            public void addKeys(Pair... keys) {
                for (Pair key : keys) {
                    this.keys.put(key.key, key.value);
                }
            }

            public ValidationRule addArgs(String key, Object value) {
                this.args.put(key, value);
                return this;
            }

            /**
             * Validation key
             */
            public Map<String, Object> getKeys() {
                return keys;
            }

            /**
             * Error message
             */
            public String getMessage() {
                return message;
            }

            /**
             * Error message arguments
             */
            public Map<String, Object> getArgs() {
                return args;
            }

            /**
             * Annotation name
             */
            public String getName() {
                return name;
            }
        }
    }

    private static class Pair {
        String key;

        Object value;

        public Pair(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

}

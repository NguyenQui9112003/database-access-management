package org.example.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.tools.*;
import java.io.*;
import org.example.annotations.Column;

public class EntityFieldGenerator {
    public static void generateAccessors(Class<?> entityClass) throws Exception {
        StringBuilder sourceCode = new StringBuilder();
        sourceCode.append("package ").append(entityClass.getPackage().getName()).append(";\n\n");
        sourceCode.append("public class ").append(entityClass.getSimpleName()).append("Accessors {\n");

        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                String fieldName = field.getName();
                String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String fieldType = field.getType().getSimpleName();

                // Generate getter
                sourceCode.append("    public ").append(fieldType).append(" get")
                         .append(capitalizedFieldName).append("() {\n")
                         .append("        return this.").append(fieldName).append(";\n")
                         .append("    }\n\n");

                // Generate setter
                sourceCode.append("    public void set").append(capitalizedFieldName)
                         .append("(").append(fieldType).append(" ").append(fieldName).append(") {\n")
                         .append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n")
                         .append("    }\n\n");
            }
        }
        sourceCode.append("}");

        // Compile the generated code
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        
        // Save source file
        String sourcePath = "generated/" + entityClass.getSimpleName() + "Accessors.java";
        try (Writer writer = new FileWriter(sourcePath)) {
            writer.write(sourceCode.toString());
        }
    }
}

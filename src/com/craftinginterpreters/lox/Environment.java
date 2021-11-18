package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   This is where our variables live - the machinery for scopes
 */
class Environment {
    // pointer to parent environment (for scope nesting)
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    // global scope doesn't have a parent...
    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        // search the variable in the current scope
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        // couldn't find the variable in the current scope? search the parent scope
        if (enclosing != null)
            return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        // if variable lives in current scope, update it
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        // else, try to update in the parent scope
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    // a new variable is always declared in the current innermost scope
    void define(String name, Object value) {
        values.put(name, value);
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }

        return environment;
    }


    Object getAt(int distance, String name) {
        return ancestor(distance).values.get(name);
    }

    List<Object> getAll(String name) {
        Environment current = this;
        List<Object> objects = new ArrayList<>();
        while (current != null) {
            if (current.values.get(name) != null) {
                objects.add(current.values.get(name));
            }
            current = current.enclosing;
        }

        return objects;
    }

    void assignAt(int distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme, value);
    }
}
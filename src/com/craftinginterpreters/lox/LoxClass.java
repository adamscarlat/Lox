package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

class LoxClass implements LoxCallable {
    final String name;
    final List<LoxClass> superclasses;
    private final Map<String, LoxFunction> methods;

    LoxClass(String name, List<LoxClass> superclasses, Map<String, LoxFunction> methods) {
        this.name = name;
        this.superclasses = superclasses;
        this.methods = methods;
    }

    LoxFunction findMethod(String name) {
        // first try to find the method in the class
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        // if we could not find it and there is a superclass, look for the method there
        if (superclasses != null && superclasses.size() > 0) {
            for (LoxClass superclass : superclasses) {
                LoxFunction method = superclass.findMethod(name);
                if (method != null) return method;
            }
            //return superclasses.findMethod(name);
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod("init");

        if (initializer == null) return 0;
        return initializer.arity();
    }
}
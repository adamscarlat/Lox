package com.craftinginterpreters.lox;

import java.util.List;

interface LoxCallable {
    // fancy word for how many args a function takes
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
package com.github.windsekirun.baseapp.module.argsinjector

/**
 * PyxInjector
 * Class: Extra
 * Created by Pyxis on 2017-10-23.
 */

@Target(AnnotationTarget.FIELD)
annotation class Argument(val value: String = "")
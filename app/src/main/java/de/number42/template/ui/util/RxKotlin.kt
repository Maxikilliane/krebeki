package de.number42.template.ui.util

import io.reactivex.Observable


inline fun <reified R : Any> Observable<in R>.ofSubType(): Observable<R> = ofType(R::class.java)
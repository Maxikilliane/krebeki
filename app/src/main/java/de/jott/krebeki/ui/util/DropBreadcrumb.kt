package de.jott.krebeki.ui.util

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single

class DropBreadcrumb<T> : ObservableTransformer<T, T> {
  override fun apply(upstream: Observable<T>): ObservableSource<T> {
    val breadcrumb = BreadcrumbException()
    return upstream.onErrorResumeNext {  t: Throwable ->
        throw CompositeException(t, breadcrumb)
      }
  }
}

fun <T> Observable<T>.dropBreadcrumb(): Observable<T> {
  val breadcrumb = BreadcrumbException()
  return this.onErrorResumeNext { error: Throwable ->
    throw CompositeException(error, breadcrumb)
  }
}

fun <T> Single<T>.dropBreadcrumb(): Single<T> {
  val breadcrumb = BreadcrumbException()
  return this.onErrorResumeNext { error: Throwable ->
    throw CompositeException(error, breadcrumb)
  }
}

fun <T> Maybe<T>.dropBreadcrumb(): Maybe<T> {
  val breadcrumb = BreadcrumbException()
  return this.onErrorResumeNext { error: Throwable ->
    throw CompositeException(error, breadcrumb)
  }
}

fun Completable.dropBreadcrumb(): Completable {
  val breadcrumb = BreadcrumbException()
  return this.onErrorResumeNext { error: Throwable ->
    throw CompositeException(error, breadcrumb)
  }
}

fun <T> Flowable<T>.dropBreadcrumb(): Flowable<T> {
  val breadcrumb = BreadcrumbException()
  return this.onErrorResumeNext { error: Throwable ->
    throw CompositeException(error, breadcrumb)
  }
}

class BreadcrumbException : Exception()
package de.number42.template.ui.util

import android.annotation.SuppressLint
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposables
import android.os.Looper
import io.reactivex.android.MainThreadDisposable

interface AdapterClickListener<D> {
  fun onClick(data: D)
}

abstract class BetterAdapter<VH: RecyclerView.ViewHolder, D>(open var clickListener:AdapterClickListener<D>?) : RecyclerView.Adapter<VH>() {

  @CheckResult
  fun clicks(): Observable<D> {
    return AdapterSelectionObservable(this)
  }

}

internal class AdapterSelectionObservable<VH: RecyclerView.ViewHolder, D>(private val adapter: BetterAdapter<VH, D>) :
    Observable<D>() {

  @SuppressLint("RestrictedApi")
  override fun subscribeActual(observer: Observer<in D>) {
    if (!checkMainThread(observer)) {
      return
    }

    val listener = Listener(adapter, observer)
    observer.onSubscribe(listener)
    adapter.clickListener = listener
  }

  internal class Listener<VH: RecyclerView.ViewHolder, D>(
    private val adapter: BetterAdapter<VH, D>,
    private val observer: Observer<in D>
  ) : MainThreadDisposable(), AdapterClickListener<D> {

    override fun onDispose() {
      adapter.clickListener = null
    }

    override fun onClick(data: D) {
      if (!isDisposed) {
        observer.onNext(data)
      }
    }
  }
}



fun checkMainThread(observer: Observer<*>): Boolean {
  if (Looper.myLooper() != Looper.getMainLooper()) {
    observer.onSubscribe(Disposables.empty())
    observer.onError(
        IllegalStateException(
            "Expected to be called on the main thread but was " + Thread.currentThread().name
        )
    )
    return false
  }
  return true
}
package tiiehenry.widget;

import android.widget.*;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import java.util.List;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import tiiehenry.widget.BaseRecyclerAdapter.Listener;

public class ViewHolder<T> extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

  // SparseArray 比 HashMap 更省内存，在某些条件下性能更好，只能存储 key 为 int 类型的数据，
  // 用来存放 View 以减少 findViewById 的次数
  private SparseArray<View> viewSparseArray;

  private View rootView;
  private T data;

  private BaseRecyclerAdapter.Listener<T> listener;
  public ViewHolder(View itemView) {
	super(itemView);
	itemView.setOnClickListener(this);
	itemView.setOnLongClickListener(this);
	rootView = itemView;
	viewSparseArray = new SparseArray<>();
  }

  public void setData(T data) {
	this.data = data;
  }

  public T getData() {
	return data;
  }

  public View getView(int viewId) {
	// 先从缓存中找，找打的话则直接返回
	// 如果找不到则 findViewById ，再把结果存入缓存中
	View view = viewSparseArray.get(viewId);
	if (view == null) {
	  view = itemView.findViewById(viewId);
	  viewSparseArray.put(viewId, view);
	}
	return view;
  }
  public View getRootView() {
	return rootView;
  }

  public void setListener(BaseRecyclerAdapter.Listener<T> lsn) {
	listener = lsn;
  }
  @Override
  public void onClick(View rootView) {
	if (listener != null)
	  listener.onItemClick(rootView, data, getAdapterPosition());
  }
  @Override
  public boolean onLongClick(View rootView) {
	if (listener != null)
	  return listener.onItemLongClick(rootView, data, getAdapterPosition());
	return true;
  }
  


}

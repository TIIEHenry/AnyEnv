package tiiehenry.widget;

import android.widget.*;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

  // SparseArray 比 HashMap 更省内存，在某些条件下性能更好，只能存储 key 为 int 类型的数据，
  // 用来存放 View 以减少 findViewById 的次数
  private SparseArray<View> viewSparseArray;

  private View rootView;

  public ViewHolder(View itemView) {
	super(itemView);
	itemView.setOnClickListener(this);
	itemView.setOnLongClickListener(this);
	rootView = itemView;
	viewSparseArray = new SparseArray<>();
  }

  /**
   * 根据 ID 来获取 View
   *
   * @param viewId viewID
   * @param <T>    泛型
   * @return 将结果强转为 View 或 View 的子类型
   */
  public <T extends View> T getView(int viewId) {
	// 先从缓存中找，找打的话则直接返回
	// 如果找不到则 findViewById ，再把结果存入缓存中
	View view = viewSparseArray.get(viewId);
	if (view == null) {
	  view = itemView.findViewById(viewId);
	  viewSparseArray.put(viewId, view);
	}
	return (T) view;
  }
  public <T extends View> T getRootView() {
	return (T) rootView;
  }

  @Override
  public void onClick(View rootView) {
	onItemClick(rootView, getAdapterPosition());
  }

  @Override
  public boolean onLongClick(View v) {
	return onItemLongClick(rootView, v, getAdapterPosition());
  }

  abstract void onItemClick(View v, int pos);

  abstract boolean onItemLongClick(View parent, View v, int pos);


}

package tiiehenry.widget;

import android.view.*;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.List;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import tiiehenry.widget.BaseRecyclerAdapter.Listener;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

  private List<T> dataList;

  private BaseRecyclerAdapter.Listener<T> listener;

  public BaseRecyclerAdapter(List<T> dataList) {
	super();
	this.dataList = dataList;
  }

  @Override
  public abstract int getItemViewType(int position)

  @Override
  public abstract ViewHolder<T> onCreateViewHolder(ViewGroup parent, int pos)

  @Override
  public void onBindViewHolder(ViewHolder<T> holder, int pos) {
	T data=dataList.get(pos);
	holder.setData(data);
	holder.setListener(listener);
	bindData(holder, data, getItemViewType(pos), pos);
  }

  public void setListener(Listener<T> lsn) {
	listener = lsn;
  }
  
  public List<T> getDataList() {
	return dataList;
  }

  @Override
  public int getItemCount() {
	return dataList.size();
  }


  public void add(T data) {
	dataList.add(data);
	notifyItemInserted(dataList.size());
  }
  public void add(int pos, T data) {
	dataList.add(pos, data);
	notifyItemInserted(pos);
  }
  public void remove(T data) {
	dataList.remove(data);
	notifyItemRemoved(dataList.indexOf(data));
  }
  public void remove(int pos) {
	dataList.remove(pos);
	notifyItemRemoved(pos);
  }
  public void clear() {
	dataList.clear();
	notifyDataSetChanged();
  }

  protected abstract void bindData(ViewHolder<T> holder, T data, int type, int pos);
  public static interface Listener<T> {
	public abstract void onItemClick(View rootView, T data, int pos);
	public abstract boolean onItemLongClick(View rootView, T data, int pos);
  }
}

package tiiehenry.widget;

import android.view.*;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {


  protected List<T> dataList;

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
	bindData(holder,data, getItemViewType(pos), pos);
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
	notifyDataSetChanged();
  }
  public void add(int pos, T data) {
	dataList.add(pos, data);
	notifyDataSetChanged();
  }
  public void remove(T data) {
	dataList.remove(data);
	notifyDataSetChanged();
  }
  public void remove(int pos) {
	dataList.remove(pos);
	notifyDataSetChanged();
  }
  public void clear() {
	dataList.clear();
	notifyDataSetChanged();
  }

  protected abstract void bindData(ViewHolder<T> holder,T data, int type, int pos);

}

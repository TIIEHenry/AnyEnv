package tiiehenry.widget;

import android.view.*;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

  protected LayoutInflater layoutInflater;

  protected List<T> dataList;

  public RecyclerAdapter(Context context, List<T> dataList) {
	this.layoutInflater = LayoutInflater.from(context);
	this.dataList = dataList;
  }

  @Override
  public int getItemViewType(int position) {
	return super.getItemViewType(position);
  }

  @Override
  public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)

  @Override
  public void onBindViewHolder(ViewHolder holder, int pos) {
	bindData(holder, dataList.get(pos), pos);
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

  abstract void bindData(ViewHolder holder, T data, int pos);

}

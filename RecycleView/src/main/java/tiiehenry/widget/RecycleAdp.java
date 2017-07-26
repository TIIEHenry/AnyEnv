package tiiehenry.widget;

import android.view.*;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public abstract class RecycleAdp<T> extends RecyclerView.Adapter<CommonViewHolder> {

  protected LayoutInflater layoutInflater;

  protected List<T> dataList;

  protected int layoutId;

  protected MultiTypeSupport<T> multiTypeSupport;

  public RecycleAdp(Context context, List<T> dataList, int layoutId) {
	this.layoutInflater = LayoutInflater.from(context);
	this.dataList = dataList;
	this.layoutId = layoutId;
  }

  @Override
  public int getItemViewType(int position) {
	if (multiTypeSupport != null) {
	  return multiTypeSupport.getLayoutId(dataList.get(position), position);
	}
	return super.getItemViewType(position);
  }

  @Override
  public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	if (multiTypeSupport != null) {
	  layoutId = viewType;
	}
	View itemView = layoutInflater.inflate(layoutId, parent, false);
	return new CommonViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(CommonViewHolder holder, int position) {
	bindData(holder, dataList.get(position));
  }

  @Override
  public int getItemCount() {
	return dataList.size();
  }

  abstract void bindData(CommonViewHolder holder, T data);

}

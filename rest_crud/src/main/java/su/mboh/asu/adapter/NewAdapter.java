package su.mboh.asu.adapter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import su.mboh.asu.R;
import su.mboh.asu.activity.ActivityNew;
import su.mboh.asu.entity.New;

/**
 * Created by smktiufa on 16/12/16.
 */

@TargetApi(Build.VERSION_CODES.N)
public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {

    private Context context;
    private List<New> newList = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    public NewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Date date = new Date(newList.get(position).getCreateDate());

        holder.title.setText(newList.get(position).getName());
        holder.date.setText(dateFormat.format(date));
        holder.content.setText(newList.get(position).getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                dialog(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public void addNew (New news){
        newList.add(news);
        notifyDataSetChanged();
    }
    public void addNewses(List<New> newlist) {
        this.newList.addAll(newlist);
        notifyDataSetChanged();
    }
    public void clearNew (){
        newList.clear();
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            date = (TextView) itemView.findViewById(R.id.text_date);
            content = (TextView) itemView.findViewById(R.id.text_content);

        }

    }

    private void dialog (final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choice_one);
        builder.setIcon(R.drawable.ic_hourglass_empty_black_24dp);
        builder.setItems(new String[]{
                        context.getString(R.string.edit),
                        context.getString(R.string.delete)},
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    ((ActivityNew)context).editNew(newList.get(position));
                                }else if(i == 1){
                                    dialogDelete(position);
                                }

                            }

                });
        builder.show();
    }
    private void dialogDelete (final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choice_one);
        builder.setIcon(R.drawable.ic_hourglass_empty_black_24dp);
        builder.setMessage("yakin?");

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ActivityNew)context).deleteNews(newList.get(position));
            }

        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();


    }
}

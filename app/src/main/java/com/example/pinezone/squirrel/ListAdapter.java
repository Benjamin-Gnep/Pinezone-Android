package com.example.pinezone.squirrel;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.SquirrelService;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ThingsItem> list;
    private int pineconeNum;
    private Context context;
    ListAdapter(List<ThingsItem> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.things_group_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        context = parent.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://111.230.173.4:8081/v1/")
                        .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                        .build();

                final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
                Call<Squirrel> call;
                int position = holder.getAdapterPosition();
                ThingsItem item = list.get(position);
                switch (item.getMode()){
                    case 1://便当的点击事件
                        FoodActivity.progressBar.setVisibility(View.VISIBLE);
                        call = squirrelService.getSquirrel(MainActivity.getUid());
                        call.enqueue(new Callback<Squirrel>() {
                            @Override
                            public void onResponse(Call<Squirrel> call, Response<Squirrel> response) {
                                String s;
                                response.body().show();
                                pineconeNum = response.body().getPinecone();
                                if(response.body().getFood() == 1){
                                    s = "便当盒现在是空的，";
                                }else {
                                    s = "便当盒里已有食物，";
                                }
                                FoodActivity.progressBar.setVisibility(View.INVISIBLE);
                                new AlertDialog.Builder(parent.getContext()).setTitle("便当确认")
                                        .setMessage(s + "你确定要花费" + item.getValue() + "个松果" +
                                                "将" + item.getTitle() + "放入便当盒中吗？便当盒" +
                                                "中只能有一份食物")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                setFood(item.getValue(),item.getAttr());
                                            }
                                        })
                                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                            @Override
                            public void onFailure(Call<Squirrel> call, Throwable t) {
                                Toast.makeText(parent.getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                                FoodActivity.progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                        break;
                    case 2://成就的点击事件
                        BagActivity.progressBar.setVisibility(View.VISIBLE);
                        call = squirrelService.getSquirrel(MainActivity.getUid());
                        call.enqueue(new Callback<Squirrel>() {
                            @Override
                            public void onResponse(Call<Squirrel> call, Response<Squirrel> response) {
                                String s;
                                response.body().show();
                                pineconeNum = response.body().getPinecone();
                                if(response.body().getCompanion() == 1 && response.body().getAchievement() == 1){
                                    s = "背包现在是空的，";
                                }else {
                                    s = "背包里已有物品，";
                                }
                                BagActivity.progressBar.setVisibility(View.INVISIBLE);
                                new AlertDialog.Builder(parent.getContext()).setTitle("背包确认")
                                        .setMessage(s + "你确定要花费" + item.getValue() + "个松果" +
                                                "将" + item.getTitle() + "放入背包中吗？背包" +
                                                "中只能有一份物品")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                setAchievement(item.getValue(),item.getAttr());
                                            }
                                        })
                                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                            @Override
                            public void onFailure(Call<Squirrel> call, Throwable t) {
                                BagActivity.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(parent.getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 3://成就的点击事件
                        BagActivity.progressBar.setVisibility(View.VISIBLE);
                        call = squirrelService.getSquirrel(MainActivity.getUid());
                        call.enqueue(new Callback<Squirrel>() {
                            @Override
                            public void onResponse(Call<Squirrel> call, Response<Squirrel> response) {
                                String s;
                                response.body().show();
                                pineconeNum = response.body().getPinecone();
                                if(response.body().getCompanion() == 1 && response.body().getAchievement() == 1){
                                    s = "背包现在是空的，";
                                }else {
                                    s = "背包里已有物品，";
                                }
                                BagActivity.progressBar.setVisibility(View.INVISIBLE);
                                new AlertDialog.Builder(parent.getContext()).setTitle("背包确认")
                                        .setMessage(s + "你确定要花费" + item.getValue() + "个松果" +
                                                "将" + item.getTitle() + "放入背包中吗？背包" +
                                                "中只能有一份物品")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                setCompanion(item.getValue(),item.getAttr());
                                            }
                                        })
                                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                            @Override
                            public void onFailure(Call<Squirrel> call, Throwable t) {
                                BagActivity.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(parent.getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }
        });
        return holder;
    }

    private void setCompanion(int value, double attr) {
        if(pineconeNum < value){
            failed();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody companionBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        RequestBody pineconeBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(pineconeNum - value));
        Call<ResponseBody> callB = squirrelService.setPinecone(uidBody,pineconeBody);
        callB.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"已经整理好背包",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> call = squirrelService.setCompanion(uidBody,companionBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setAchievement(int value, double attr) {
        if(pineconeNum < value){
            failed();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody achievementBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        RequestBody pineconeBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(pineconeNum - value));
        Call<ResponseBody> callB = squirrelService.setPinecone(uidBody,pineconeBody);
        callB.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"已经整理好背包",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> call = squirrelService.setAchievement(uidBody,achievementBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setFood(int value, double attr) {
        if(pineconeNum < value){
            failed();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody foodBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        RequestBody pineconeBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(pineconeNum - value));
        Call<ResponseBody> callB = squirrelService.setPinecone(uidBody,pineconeBody);
        callB.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"已经整理好便当",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> call = squirrelService.setFood(uidBody,foodBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void failed() {
        new AlertDialog.Builder(context).setTitle("穷鬼")
                .setMessage("松果不够还想买东西？松果可以通过阅读文章、发布文章获得，赶快去赚松果吧！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThingsItem item = list.get(position);
        holder.icon.setImageResource(item.getImageId());
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.value.setText(String.valueOf(item.getValue()));
        holder.mode = item.getMode();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView icon;
        TextView title;
        TextView subTitle;
        TextView value;
        int mode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_tv);
            title = itemView.findViewById(R.id.title_tv);
            subTitle = itemView.findViewById(R.id.subtitle_tv);
            value = itemView.findViewById(R.id.pinecone_tv);
            this.itemView = itemView;
        }
    }

    public static void setAchievement(int attr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody achievementBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        Call<ResponseBody> call = squirrelService.setAchievement(uidBody,achievementBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void setFood(int attr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody foodBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        Call<ResponseBody> call = squirrelService.setFood(uidBody,foodBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void setCompanion(int attr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody companionBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        Call<ResponseBody> call = squirrelService.setCompanion(uidBody,companionBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void setHp(int attr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody hpBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        Call<ResponseBody> call = squirrelService.setHp(uidBody,hpBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void setPinecone(int attr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        RequestBody uidBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(MainActivity.getUid()));
        RequestBody pineconeBody = RequestBody.create
                (MediaType.parse("multipart/form-data"),
                        String.valueOf(attr));
        Call<ResponseBody> call = squirrelService.setPinecone(uidBody,pineconeBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}

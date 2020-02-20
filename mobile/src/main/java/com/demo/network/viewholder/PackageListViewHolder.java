package com.demo.network.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.network.model.Data;
import com.selecttvapp.R;
import com.selecttvapp.ui.activities.WebBrowserActivity;


public class PackageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name;
    public ImageView image;
    public ProgressBar progressBar;
    public Button btnInstall;
    public Button btnGoToApp;
    private Context context;

    public PackageListViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        name = (TextView) itemView.findViewById(R.id.ui_main_item_title);
        image = (ImageView) itemView.findViewById(R.id.ui_main_item_img);
        progressBar = (ProgressBar) itemView.findViewById(R.id.ui_pb);
        btnInstall = (Button) itemView.findViewById(R.id.ui_main_item_install_btn);
        btnGoToApp = (Button) itemView.findViewById(R.id.ui_main_item_got_to_btn);
        setListeners();
    }

    public void setListeners() {
        btnInstall.setOnClickListener(this);
        btnGoToApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Data tag = (Data) view.getTag();
        switch (view.getId()) {

            case R.id.ui_main_item_install_btn:
                manageInstall(tag);
                break;
            case R.id.ui_main_item_got_to_btn:
                openApp(tag);
                break;
            default:
                break;

        }
    }

    private void openApp(Data data) {
        try {
            if (data.isAppInstalled()) {
                openAppFromPhone(data);
            } else {
                openAppFromLink(data);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openAppFromLink(Data data) {
        final String appPackageName = data.getPackageName();
        if (appPackageName != null) {
            Intent intent = new Intent(context, WebBrowserActivity.class);
            //Intent intent = new Intent(context, WebBrowserActivityNew.class);
            intent.putExtra("url", "https://play.google.com/store/apps/details?id=" + appPackageName);
            intent.putExtra("name", data.getName());
            context.startActivity(intent);
           /* try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }*/
        } else {
            Toast.makeText(context, "package not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAppFromPhone(Data data) {
        if (data.getPackageName() != null) {
            Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(data.getPackageName());
            context.startActivity(LaunchIntent);
        } else {
            Toast.makeText(context, "package not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void manageInstall(Data tag) {
        try {
            if (tag.isAppInstalled()) {
                uninstallApp(tag);
            } else {
                openAppFromLink(tag);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void uninstallApp(Data tag) {
        if (tag.getPackageName() != null) {
            Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            intent.setData(Uri.parse("package:" + tag.getPackageName()));
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "package not available", Toast.LENGTH_SHORT).show();
        }
    }
}

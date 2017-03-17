package org.zhonghao.gps.biz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import org.zhonghao.gps.entity.UpdateInfo;

import java.io.File;

public class UpdateBiz {
	ProgressDialog pBar;
	Dialog dialog;
	final Bundle bundle=new Bundle();

	public void downloadApk(final String apkUrl, final Handler handler, final Context context)
	{
		RequestParams requestParams=new RequestParams(apkUrl);
		String sdcardRoot= Environment.getExternalStorageDirectory().getAbsolutePath();
		final String apkSavePath=sdcardRoot+"/AnTu.apk";
		Log.i("down", apkSavePath);
		File apkFile=new File(apkSavePath);
		if (apkFile.exists())
		{
			apkFile.delete();
		}
		requestParams.setSaveFilePath(apkSavePath);
		x.http().get(requestParams,new Callback.ProgressCallback<File>(){
			@Override
			public void onSuccess(File file) {
				Log.i("升级003", file.getAbsolutePath());
				Message message=handler.obtainMessage();
//				message.what= MapActivity.MSG_INSTALL;
				Bundle bundle=new Bundle();
				bundle.putString("apkSavePath", apkSavePath);
				message.setData(bundle);
				handler.sendMessage(message);
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				Log.i("升级出错", throwable.getMessage());
				throwable.printStackTrace();

			}

			@Override
			public void onCancelled(CancelledException e) {

			}

			@Override
			public void onFinished() {
				Log.d("down","更新结束");
				pBar.cancel();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("下载完成");
				builder.setMessage("是否安装？");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("down","安装路径"+apkSavePath);
						// android有安装apk的activity
						Intent intent = new Intent(Intent.ACTION_VIEW);
						File apkFile = new File(apkSavePath);
						Uri uri = Uri.fromFile(apkFile);
						//mime:文件类型
						String type = "application/vnd.android.package-archive";
						intent.setDataAndType(uri,type);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}


				});
				AlertDialog dialog = builder.create();
				dialog.show();



			}

			@Override
			public void onWaiting() {
				Log.d("down","等待更新");

			}

			@Override
			public void onStarted() {
				Log.d("down","更新start");
				pBar = new ProgressDialog(context);
				pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pBar.setTitle("正在下载");
				pBar.setMessage("请稍候...");
				pBar.setProgress(0);
				pBar.setMax(100);
				pBar.show();

			}

			@Override
			public void onLoading(long n, long m, boolean b) {
				int progress = (int) (100*m/n);
				pBar.setProgress(progress);

			}
		});


	}
	UpdateInfo info ;
	public void getNewVersion(final Handler handler, final Context context) {

		try {
			// 用什么框架联网步骤
			// 1,连谁
			// 新的apk刚开发出来，可能有bug,不能让所有用户都升级
			//String url = "http://117.158.206.86:8681/NewGPSTrace2.0/update/update.txt";
			String url = "http://gps.zhonghaokeji.cn/NewGPSTrace2.0/update/update.txt";
			RequestParams requestParams = new RequestParams(url);
			Log.i("resquenseP", requestParams.toString());


			x.http().get(requestParams, new CommonCallback<String>() {

				@Override
				public void onSuccess(String result) {
					Log.i("升级001", result);
					UpdateInfo updateInfo=null;
					try {
						Log.i("升级001", context.toString());
						String info = result;
						Log.i("升级001", info);
						updateInfo = new UpdateInfo();
						updateInfo.setVersion(info.split("&")[1]);
						Log.i("升级001",updateInfo.getVersion());
						updateInfo.setDescription(info.split("&")[2]);
						updateInfo.setUrl(info.split("&")[3]);
						Log.i("升级112", updateInfo.getUrl());

					} catch (Exception e) {
						// TODO: handle exception
					}finally
					{
						Message message=handler.obtainMessage();
//						message.what=AdministratorIndexActivity.MSG_SHOW;
						//message.obj=versionEntity;
						message.what = 4;

						bundle.putSerializable("versionEntity", updateInfo);
						Log.d("buddle",updateInfo.getVersion());
						message.setData(bundle);
						handler.sendMessage(message);
					}


				}

				@Override
				public void onError(Throwable ex, boolean isOnCallback) {
					// TODO Auto-generated method stub
					ex.printStackTrace();
				}

				@Override
				public void onCancelled(CancelledException cex) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFinished() {
					// TODO Auto-generated method stub

				}
			});

		} catch (Exception e) {

		}
	}

}

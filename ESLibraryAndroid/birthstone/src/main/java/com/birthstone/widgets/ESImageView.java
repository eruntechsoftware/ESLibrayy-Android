package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.*;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.disk.FileCache;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.LinkedList;

/**
 * 图片显示控件
 */
public class ESImageView extends SimpleDraweeView implements IDataInitialize,ICollectible, IValidatible, IReleasable
{
	protected String mCollectSign;
	protected Activity mActivity;
	protected String mName;
	protected String mURI;
	protected int srcid;
	public static String IMAGE_URL_HEAD = "";
	private FileCache fileCache;

	public ESImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESImageView);
		mCollectSign = a.getString(R.styleable.ESImageView_collectSign);
		srcid = a.getResourceId(R.styleable.ESImageView_srcid,0);
		setImageResource(srcid);
		a.recycle();
	}

	/**
	 * 获取网络地址头段
	 **/
	public static String getImageUrlHead()
	{
		return IMAGE_URL_HEAD;
	}

	/**
	 * 显示网络图片
	 *
	 * @param urlBody 图片网络尾地址
	 */
	public void setImageWithUrlBody(String urlBody)
	{
		String imageUrl = IMAGE_URL_HEAD + urlBody;
		this.setImageURI(Uri.parse(imageUrl));
	}

	public void setImageURI(Uri uri)
	{
		mURI = uri.toString();
		super.setImageURI(uri);
	}

	public void dataInitialize ()
	{

		if (mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
		fileCache = Fresco.getImagePipelineFactory().getMainFileCache();
	}

	public Object getActivity ()
	{
		return mActivity;
	}

	public void setActivity (Object arg0)
	{
		if (arg0 instanceof Activity)
		{
			mActivity = (Activity) arg0;
		}
	}

	public LinkedList<String> getRequest ()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	public void release (String dataName, Data data)
	{
		if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if(!data.getStringValue().equals(""))
			{
				setImageURI(data.getStringValue());
				mURI = data.getStringValue();
			}
		}
	}

	/**
	 *获取由URI返回的文件对象
	 * **/
	public File getImageFile()
	{
		File file = null;
		if(mURI!=null && mURI.length()>0)
		{
			FileBinaryResource resource = (FileBinaryResource)fileCache.getResource(new SimpleCacheKey(mURI.toString()));
			if(resource!=null)
			{
				file = resource.getFile();
			}
		}
		if(file==null && mURI!=null && mURI.trim().length()>0)
		{
			return new File(mURI.replace("file:",""));
		}

		return file;
	}

	public DataCollection collect ()
	{
		DataCollection datas = new DataCollection();
//		if (this.mURI.equals(""))
//		{
//			datas.add(new Data(this.mName, ""));
//		}
//		else
//		{
//			datas.add(new Data(mName, mURI, DataType.String));
//		}
		return datas;
	}

	public String[] getCollectSign ()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public void setCollectSign(String mCollectSign)
	{
		this.mCollectSign = mCollectSign;
	}

	@Override
	public String getName()
	{
		return mName;
	}

	@Override
	public Boolean dataValidator()
	{
		return true;
	}

	/**
	 * 提示校验错误
	 * **/
	public void hint()
	{
//		ToastHelper.toastShow(this.getContext(),getHint().toString());
	}
}

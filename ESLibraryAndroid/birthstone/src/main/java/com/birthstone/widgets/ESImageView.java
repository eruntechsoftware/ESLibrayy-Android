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
import com.birthstone.core.interfaces.*;
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
public class ESImageView extends SimpleDraweeView implements IDataInitialize, ICollectible, IValidatible, IReleasable
{
	protected String mCollectSign;
	protected IChildView mActivity;
	protected Boolean mIsRequired;
	protected Boolean isEmpty = true;
	protected String mName;
	protected String mImage_Uri;
	protected String mMessage="";
	protected int srcid;
	public static String IMAGE_URL_HEAD = "";
	private FileCache fileCache;

	public ESImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESImageView);
		mCollectSign = a.getString(R.styleable.ESImageView_collectSign);
		mMessage = a.getString(R.styleable.ESTextBox_message);
		mIsRequired = a.getBoolean(R.styleable.ESTextBox_isRequired, false);
		srcid = a.getResourceId(R.styleable.ESImageView_srcid, 0);
		if(srcid>0)
		{
			isEmpty=false;
		}
		mImage_Uri = a.getString(R.styleable.ESImageView_image_uri);
		setImageResource(srcid);
		if(!"".equals(mImage_Uri.trim()))
		{
			isEmpty=false;
		}
		super.setImageURI(mImage_Uri);
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
		isEmpty=false;
	}

	public void setImageURI(Uri uri)
	{
		mImage_Uri = uri.toString();
		super.setImageURI(uri);
		isEmpty=false;
	}

	public void dataInitialize()
	{
		if (mActivity != null)
		{
			String classnameString = ((Context) mActivity).getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
		fileCache = Fresco.getImagePipelineFactory().getMainFileCache();
	}

	public Object getChildView()
	{
		return mActivity;
	}

	public void setChildView(Object arg0)
	{
		mActivity = (IChildView) arg0;

	}

	public LinkedList<String> getRequest()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	public void release(String dataName, Data data)
	{
		if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if (!data.getStringValue().equals(""))
			{
				setImageURI(data.getStringValue());
				mImage_Uri = data.getStringValue();
			}
		}
	}

	/**
	 * 获取由URI返回的文件对象
	 **/
	public File getImageFile()
	{
		File file = null;
		if (mImage_Uri != null && mImage_Uri.length() > 0)
		{
			if(fileCache == null )
			{
				fileCache = Fresco.getImagePipelineFactory().getMainFileCache();
			}
			FileBinaryResource resource = (FileBinaryResource) fileCache.getResource(new SimpleCacheKey(mImage_Uri.toString()));
			if (resource != null)
			{
				file = resource.getFile();
			}
		}
		if (file == null && mImage_Uri != null && mImage_Uri.trim().length() > 0)
		{
			return new File(mImage_Uri.replace("file:", ""));
		}

		return file;
	}

	public DataCollection collect()
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

	public String[] getCollectSign()
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

	@Override
	public Boolean getIsRequired() {
		return mIsRequired;
	}

	@Override
	public Boolean getIsEmpty() {
		return isEmpty;
	}

	/**
	 * 提示校验错误
	 **/
	public void message()
	{
		ToastHelper.toastShow(this.getContext(),mMessage);
	}

	@Override
	public void expressionMessage() {
		//ToastHelper.toastShow(this.getContext(),mExpressionMessage);
	}


}

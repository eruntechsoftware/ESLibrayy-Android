package com.birthstone.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birthstone.R;


/***
 * 日期：2015年10月16日
 * <p>
 * 作者：张景瑞
 * <p>
 * 功能：屏幕中央弹出式列表
 */
public class ESActionPopup extends PopupWindow implements View.OnClickListener
{
    /**
     * 声明控件
     **/
    private View view, parent, dividerline;
    private RelativeLayout dialog_rootView;
    private LinearLayout viewGroup;
    private LinearLayout divider;
    private TextView poptitle;


    /**
     * 私有变量声明
     **/
    private Context context;
    private OnActionPopupClickListener onActionPopupClickListener;
    private String title = "";
    private Object[] items;
    private View.OnClickListener onCancelButtonClickListener;
    private int titleColor,itemColor;
    /***
     * ESActionPopup
     *
     * @param context
     * @param parent 父视图
     * @param items   ActionPopup项目
     * @param title 标题
     */
    public ESActionPopup (Context context, View parent, Object[] items, String title)
    {
        super(context);
        this.context = context;
        this.parent = parent;
        setParams();
        this.title = title;
        this.items = items;
        if (this.items != null && this.items.length > 0)
        {
            createMenu();
        }
    }

    /***
     * ESActionPopup
     *
     * @param context
     * @param items   ActionPopup项目
     */
    public ESActionPopup (Context context, View parent, Object[] items)
    {
        super(context);
        this.context = context;
        this.parent = parent;
        setParams();
        this.items = items;
        if (this.items != null && this.items.length > 0)
        {
            createMenu();
        }
    }

    /***
     * 日期：2015年10月16日
     * 作者：张景瑞
     * 功能：设置必要参数
     */
    protected void setParams ()
    {
        view = View.inflate(context, R.layout.es_actionpopup, null);
        setContentView(view);

        viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);
        poptitle = (TextView) view.findViewById(R.id.title);
        dividerline = (View) view.findViewById(R.id.divider);
        dialog_rootView = (RelativeLayout) view.findViewById(R.id.dialog_rootView);
        dialog_rootView.setOnClickListener(this);
    }

    /***
     * 日期：2015年10月16日
     * 作者：张景瑞
     * 功能：创建弹出菜单
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void createMenu ()
    {
        if (title.equals(""))
        {
            poptitle.setVisibility(View.GONE);
            dividerline.setVisibility(View.GONE);
        }
        else
        {
            poptitle.setText(title);
            poptitle.setVisibility(View.VISIBLE);
            dividerline.setVisibility(View.VISIBLE);
        }
        if (this.items != null && this.items.length > 0)
        {
            int size = items.length;
            for (int i = 0; i < size; i++)
            {
                //添加Button
                Button btnItem = new Button(context);
                //设置view的ID
                btnItem.setId(i);
                btnItem.setText(items[i].toString());

                //添加事件监听
                btnItem.setOnClickListener(this);

                //设置button布局
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                btnItem.setLayoutParams(layoutParams);
                btnItem.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //设置字体样式
                if (i == size - 1)
                {
                    btnItem.setTextColor(context.getResources().getColor(R.color.es_text_blank));
                }
                else
                {
                    btnItem.setTextColor(context.getResources().getColor(R.color.es_text_blank));
                }
                btnItem.setTextSize(14.0f);
                btnItem.setPadding(0, 30, 0, 30);

                viewGroup.addView(btnItem);

                //设置间隔线及样式
                if (i < size - 1)
                {
                    divider = new LinearLayout(context);
                    divider.setBackgroundColor(context.getResources()
                                                       .getColor(R.color.es_divider_line));

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, 0);
                    lp.height = 1;
                    divider.setLayoutParams(lp);

                    viewGroup.addView(divider);
                }

            }
        }

    }

    /***
     * 日期：2015年10月16日
     * 作者：张景瑞
     * 功能：显示CenterWindow
     */
    public void show ()
    {
        // set  enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_dialog_main_show_amination));
        viewGroup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_dialog_root_show_amin));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
    }

    public void onClick (View v)
    {
        if (v.getId() == R.id.dialog_rootView)
        {
            dismiss();
        }
        if (v.getId() < items.length)
        {
            dismiss();
            if (onActionPopupClickListener != null)
            {
                onActionPopupClickListener.onClick(v);
            }
        }
    }

    /***
     * 日期：2015年10月16日
     * 作者：张景瑞
     * 功能：隐藏CenterWindow
     */
    public void dismiss ()
    {

        //设置动画效果
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.es_dialog_main_hide_amination);
        anim.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart (Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat (Animation animation)
            {
            }

            @Override
            public void onAnimationEnd (Animation animation)
            {
                view.post(new Runnable()
                {
                    @Override
                    public void run ()
                    {
                        ESActionPopup.super.dismiss();
                    }
                });

            }
        });

        viewGroup.startAnimation(anim);

    }


    /***
     * 日期：2015年10月16日
     * 作者：张景瑞
     * 功能：设置ActionPopup单击处理监听对象
     *
     * @param onActionPopupClickListener 监听器对象
     */
    public void setOnActionPopupClickListener (OnActionPopupClickListener onActionPopupClickListener)
    {
        this.onActionPopupClickListener = onActionPopupClickListener;
    }

    /**
     * 日期：2015年10月16日
     * 作者：张景瑞
     * 功能：CenterDialog单击事件监听器
     */
    public interface OnActionPopupClickListener
    {
        public void onClick (View view);
    }

    /**
     * 设置标题颜色
     * @param titleColor 标题颜色
     * **/
    public void setTitleColor(int titleColor)
    {
        this.titleColor = titleColor;
        if(poptitle!=null)
        {
            poptitle.setTextColor(titleColor);
        }
    }

    /**
     * 设置菜单颜色
     * @param itemColor 标题颜色
     * **/
    public void setItemColor (int itemColor)
    {
        this.itemColor = itemColor;
        if(viewGroup!=null && viewGroup.getChildCount()>0)
        {
            int size = viewGroup.getChildCount();
            for(int i=0; i<size; i++)
            {
                Button btnItem = (Button)viewGroup.getChildAt(i);
                btnItem.setTextColor(itemColor);
            }
        }
    }
}

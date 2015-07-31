package gtrj.shbz.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gtrj.shbz.R;

/**
 * Created by zhang77555 on 2015/7/31.
 */
public class zTextView extends FrameLayout{
    private RelativeLayout zView;
    private TextView zText;
    private Boolean selected=false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public zTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.z_text_view, this);
        zView=(RelativeLayout)findViewById(R.id.z_view);
        zText=(TextView)findViewById(R.id.z_text);
        TypedArray text = context.obtainStyledAttributes(attrs,R.styleable.zTextView);
        setzText(text.getString(R.styleable.zTextView_text));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void click(Boolean isSelected){
        selected=isSelected;
        if(isSelected){
            zView.setBackground(this.getResources().getDrawable(R.drawable.bg_border_select));
            zText.setTextColor(this.getResources().getColor(R.color.white));
        }else{
            zView.setBackground(this.getResources().getDrawable(R.drawable.bg_border));
            zText.setTextColor(this.getResources().getColor(R.color.text));
        }
    }

    public void setzText(String zText) {
        this.zText.setText(zText);
    }
}

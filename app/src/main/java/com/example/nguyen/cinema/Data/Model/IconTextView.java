package com.example.nguyen.cinema.Data.Model;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class IconTextView extends android.support.v7.widget.AppCompatTextView {

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public IconTextView(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        Typeface typeFaceFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(this, typeFaceFont);

//        this.setShadowLayer(1.5f, 5, 5, getContext().getResources().getColor(R.color.black_shadow));
    }
}
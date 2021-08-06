package com.zee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.FontRes;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zee.libs.R;
import com.zee.utils.UIUtils;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑马灯
 */
public class ZxMarqueeView extends ViewFlipper {

    private int interval = 3000;
    private int animDuration = 1000;
    private int textSize = 14;
    private int textColor = 0xff000000;
    private boolean singleLine = false;

    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int GRAVITY_LEFT = 0;
    private static final int GRAVITY_CENTER = 1;
    private static final int GRAVITY_RIGHT = 2;

    private int direction = DIRECTION_BOTTOM_TO_TOP;
    private static final int DIRECTION_BOTTOM_TO_TOP = 0;
    private static final int DIRECTION_TOP_TO_BOTTOM = 1;
    private static final int DIRECTION_RIGHT_TO_LEFT = 2;
    private static final int DIRECTION_LEFT_TO_RIGHT = 3;

    private Typeface typeface;

    @AnimRes
    private int inAnimResId = -1;
    @AnimRes
    private int outAnimResId = -1;

    private int position;
    private List<CharSequence> messages = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ZxMarqueeView(Context context) {
        this(context, null);
    }

    public ZxMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZxMarqueeView, defStyleAttr, 0);

        interval = typedArray.getInteger(R.styleable.ZxMarqueeView_zv_interval_time, interval);
//         typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);//检测是否有一个属性
        animDuration = typedArray.getInteger(R.styleable.ZxMarqueeView_zv_animDuration, animDuration);
        singleLine = typedArray.getBoolean(R.styleable.ZxMarqueeView_zv_singleLine, false);
        if (typedArray.hasValue(R.styleable.ZxMarqueeView_zv_textSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.ZxMarqueeView_zv_textSize, textSize);
            textSize=UIUtils.pxToSp(textSize);
        }
        textColor = typedArray.getColor(R.styleable.ZxMarqueeView_zv_textColor, textColor);
        @FontRes int fontRes = typedArray.getResourceId(R.styleable.ZxMarqueeView_zv_font, 0);
        if (fontRes != 0) {
            typeface = ResourcesCompat.getFont(context, fontRes);
        }
        int gravityType = typedArray.getInt(R.styleable.ZxMarqueeView_zv_gravity, GRAVITY_LEFT);
        switch (gravityType) {
            case GRAVITY_LEFT:
                gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }

        if (typedArray.hasValue(R.styleable.ZxMarqueeView_zv_direction)) {
            direction = typedArray.getInt(R.styleable.ZxMarqueeView_zv_direction, direction);
        }

        typedArray.recycle();
        setFlipInterval(interval);
    }

    /**
     * 根据字符串，启动翻页公告
     *
     * @param message 字符串
     */
    public void startWithText(String message) {
        startWithText(message, inAnimResId, outAnimResId);
    }

    /**
     * 根据字符串，启动翻页公告
     *
     * @param message      字符串
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    public void startWithText(final String message, final @AnimRes int inAnimResId, final @AnimRes int outAnimResID) {
        if (TextUtils.isEmpty(message)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startWithFixedWidth(message, inAnimResId, outAnimResID);
            }
        });
    }

    /**
     * 根据字符串和宽度，启动翻页公告
     *
     * @param message 字符串
     */
    private void startWithFixedWidth(String message, @AnimRes int inAnimResId, @AnimRes int outAnimResID) {
        int messageLength = message.length();
        int width = UIUtils.pxToDp(getWidth());
        if (width == 0) {
            throw new RuntimeException("Please set the width of ZxMarqueeView !");
        }
        int limit = width / textSize;
        List list = new ArrayList();

        if (messageLength <= limit) {
            list.add(message);
        } else {
            int size = messageLength / limit + (messageLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= messageLength ? messageLength : (i + 1) * limit);
                list.add(message.substring(startIndex, endIndex));
            }
        }

        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.clear();
        messages.addAll(list);
        postStart(inAnimResId, outAnimResID);
    }

    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param messages 字符串列表
     */
    public void startWithList(List<CharSequence> messages) {
        startWithList(messages, inAnimResId, outAnimResId);
    }

    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param messages     字符串列表
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    public void startWithList(List<CharSequence> messages, @AnimRes int inAnimResId, @AnimRes int outAnimResID) {
        if (ZListUtils.isEmpty(messages)) return;
        setMessages(messages);
        postStart(inAnimResId, outAnimResID);
    }

    private void postStart(final @AnimRes int inAnimResId, final @AnimRes int outAnimResID) {
        post(new Runnable() {
            @Override
            public void run() {
                start(inAnimResId, outAnimResID);
            }
        });
    }

    private boolean isAnimStart = false;

    private void start(final @AnimRes int inAnimResId, final @AnimRes int outAnimResID) {
        removeAllViews();
        clearAnimation();
        // 检测数据源
        if (messages == null || messages.isEmpty()) {
            throw new RuntimeException("The messages cannot be empty!");
        }
        position = 0;
        addView(createTextView(messages.get(position)));

        if (messages.size() > 1) {
            setInAndOutAnimation();
            startFlipping();
        }

        if (getInAnimation() != null) {
            getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (isAnimStart) {
                        animation.cancel();
                    }
                    isAnimStart = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    position++;
                    if (position >= messages.size()) {
                        position = 0;
                    }
                    View view = createTextView(messages.get(position));
                    if (view.getParent() == null) {
                        addView(view);
                    }
                    isAnimStart = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private TextView createTextView(CharSequence marqueeItem) {
        TextView textView = (TextView) getChildAt((getDisplayedChild() + 1) % 3);
        if (textView == null) {
            textView = new TextView(getContext());
            textView.setGravity(gravity | Gravity.CENTER_VERTICAL);
            textView.setTextColor(textColor);
            textView.setTextSize(textSize);

            textView.setIncludeFontPadding(true);
            textView.setSingleLine(singleLine);
            if (singleLine) {
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
            }
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getPosition(), (TextView) v);
                    }
                }
            });
        }
        CharSequence message = "";
        if (marqueeItem instanceof CharSequence) {
            message = (CharSequence) marqueeItem;
        }
        textView.setText(message);
        textView.setTag(position);
        return textView;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<CharSequence> getMessages() {
        return messages;
    }

    public void setMessages(List<CharSequence> messages) {
        this.messages = messages;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }

    private void setInAndOutAnimation() {
        if (inAnimResId != -1) {
            Animation inAnim = AnimationUtils.loadAnimation(getContext(), inAnimResId);
            inAnim.setDuration(animDuration);
            setInAnimation(inAnim);

            Animation outAnim = AnimationUtils.loadAnimation(getContext(), outAnimResId);
            outAnim.setDuration(animDuration);
            setOutAnimation(outAnim);
        } else {
            switch (direction) {
                case DIRECTION_BOTTOM_TO_TOP: //下面出来，上面消失
                    setInAnimation(getBottomIn(animDuration));
                    setOutAnimation(getBottomOut(animDuration));
                    break;
                case DIRECTION_TOP_TO_BOTTOM:
                    setInAnimation(getTopIn(animDuration));
                    setOutAnimation(getTopOut(animDuration));
                    break;
                case DIRECTION_RIGHT_TO_LEFT:
                    setInAnimation(getRightIn(animDuration));
                    setOutAnimation(getRightOut(animDuration));
                    break;

                case DIRECTION_LEFT_TO_RIGHT:
                    setInAnimation(getLeftIn(animDuration));
                    setOutAnimation(getLeftOut(animDuration));
                    break;
            }
        }
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    private AnimationSet getLeftIn(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(0, 1);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        as.addAnimation(ta);
        return as;
    }

    private AnimationSet getLeftOut(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(1, 0);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                1f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        as.addAnimation(ta);
        return as;
    }

    private AnimationSet getRightIn(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(0, 1);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        as.addAnimation(ta);
        return as;
    }

    private AnimationSet getRightOut(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(1, 0);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        as.addAnimation(ta);
        as.reset();
        return as;
    }


    private AnimationSet getTopIn(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(0, 1);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0f);

        as.addAnimation(ta);
        return as;
    }

    private AnimationSet getTopOut(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(1, 0);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);

        as.addAnimation(ta);
        as.reset();
        return as;
    }


    private AnimationSet getBottomIn(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(0, 1);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0f);

        as.addAnimation(ta);
        return as;
    }

    private AnimationSet getBottomOut(long durationMillis) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(durationMillis);

        AlphaAnimation sa = new AlphaAnimation(1, 0);
        as.addAnimation(sa);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);

        as.addAnimation(ta);
        as.reset();
        return as;
    }
}

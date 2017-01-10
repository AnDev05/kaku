package ca.fuwafuwa.kaku.Windows.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ca.fuwafuwa.kaku.KakuTools;
import ca.fuwafuwa.kaku.R;

/**
 * Created by 0x1bad1d3a on 1/9/2017.
 */

public class ChoiceImageView extends ImageView {

    private Context mContext;
    private KanjiCharacterView mKanjiView;

    private enum ChoiceType {
        EDIT,
        DELETE,
        NONE
    }

    public ChoiceImageView(Context context) {
        super(context);
        Init(context);
    }

    public ChoiceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public ChoiceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    public ChoiceImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context);
    }

    private void Init(Context context){
        setLayoutParams(new RelativeLayout.LayoutParams(KakuTools.dpToPx(context, 35), KakuTools.dpToPx(context, 35)));
    }

    public void onKanjiViewScroll(int statusBarHeight, KanjiCharacterView kanjiView, MotionEvent e1, MotionEvent e2){

        // No need to update layout as we are in the same onScroll, just check collision
        if (mKanjiView != null && mKanjiView == kanjiView){
            switch (getChoiceType(kanjiView, e2)){
                case EDIT:
                    setImageResource(R.drawable.icon_delete);
                    break;
                case DELETE:
                    setImageResource(R.drawable.icon_edit);
                    break;
                case NONE:
                    setImageResource(R.drawable.icon_swap);
                    break;
            }
            return;
        }

        mKanjiView = kanjiView;
        int[] pos = kanjiView.getOrigPosRaw();
        setX(pos[0]);
        setY(pos[1] - statusBarHeight);
        setVisibility(View.VISIBLE);
    }

    public void onKanjiViewScrollEnd(KanjiCharacterView kanjiView, MotionEvent e){

        if (mKanjiView == null){
            return;
        }

        setVisibility(INVISIBLE);

        if (mKanjiView == kanjiView) {
            mKanjiView = null;
        }
    }

    private ChoiceType getChoiceType(KanjiCharacterView kanjiView, MotionEvent e){

        int[] pos = kanjiView.getOrigPosRaw();

        if (e.getRawY() < pos[1]){
            int mid =  pos[0] + kanjiView.getWidth() / 2;
            if (e.getRawX() < mid){
                return ChoiceType.DELETE;
            }
            else {
                return ChoiceType.EDIT;
            }
        }

        return ChoiceType.NONE;
    }
}
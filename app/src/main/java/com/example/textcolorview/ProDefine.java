



    package com.example.textcolorview;
    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Paint;
    import android.graphics.RectF;
    import android.util.AttributeSet;
    import android.view.View;


    /**
     * 重写progressView类
     * @author welive
     *
     */
    @SuppressLint("DrawAllocation")
    public class ProDefine extends View {

        /**分段颜色*/
        @SuppressWarnings("unused")
        private static final int[] SECTION_COLORS = {R.color.colorPrimaryDark,R.color.colorPrimaryDark,R.color.colorPrimaryDark};//进度条的颜色渐变，本项目中用同个颜色
        /**进度条最大值*/
        private float maxCount;
        /**进度条当前值*/
        private float currentCount;
        /**画笔*/
        private Paint mPaint;
        private int mWidth,mHeight;

        int progressbarid = R.color.colorPrimary;

        int backgroundid = R.color.colorPrimary;

        float section ;

        public ProDefine(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView(context);
        }

        public ProDefine(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }

        public ProDefine(Context context) {
            super(context);
            initView(context);
        }

        private void initView(Context context) {

        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            int round = mHeight/2;
            System.out.println("max="+maxCount + "  current="+currentCount);
            //画进度条的边框线
		/*
//		 mPaint.setColor(Color.rgb(71, 76, 80));
        mPaint.setColor(getResources().getColor(R.color.progress));
		RectF rectBg = new RectF(0, 0, mWidth, mHeight);
		canvas.drawRoundRect(rectBg, round, round, mPaint);*/
            //以下三行是整条进度条的颜色
            mPaint.setColor(getResources().getColor(backgroundid));
            RectF rectBlackBg = new RectF(2, 2, mWidth-2, mHeight-2);
            canvas.drawRoundRect(rectBlackBg, round, round, mPaint);




            section = currentCount/maxCount;
            //以下三行是所占百分比进度条的颜色
            mPaint.setColor(getResources().getColor(progressbarid));
            RectF rectProgressBg = new RectF(2, 2, (mWidth-2)*section, mHeight-2);
            canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
		/*if(section <= 1.0f/3.0f){
			if(section != 0.0f){
				mPaint.setColor(SECTION_COLORS[0]);
			}else{
				mPaint.setColor(getResources().getColor(R.color.datactivity_textTime_bgcolor));
			}
		}else{
			int count = (section <= 1.0f/3.0f*2 ) ? 2 : 3;
			int[] colors = new int[count];
			System.arraycopy(SECTION_COLORS, 0, colors, 0, count);
			float[] positions = new float[count];
			if(count == 2){
				positions[0] = 0.0f;
				positions[1] = 1.0f-positions[0];
			}else{
				positions[0] = 0.0f;
				positions[1] = (maxCount/3)/currentCount;
				positions[2] = 1.0f-positions[0]*2;
			}
			positions[positions.length-1] = 1.0f;
			LinearGradient shader = new LinearGradient(3, 3, (mWidth-3)*section, mHeight-3, colors,null, Shader.TileMode.MIRROR);
			mPaint.setShader(shader);
		}
		canvas.drawRoundRect(rectProgressBg, round, round, mPaint);*/
        }

        /**
         * 密度值
         * @param dip
         * @return
         */
        private int dipToPx(int dip) {
            float scale = getContext().getResources().getDisplayMetrics().density;
            return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
        }

        /***
         * 设置最大的进度值
         * @param maxCount
         */
        public void setMaxCount(float maxCount) {
            this.maxCount = maxCount;
        }

        /***
         * 设置当前的进度值
         * @param currentCount
         */
        public void setCurrentCount(float currentCount) {
            this.currentCount = currentCount > maxCount ? maxCount : currentCount;
            invalidate();
        }

        /**
         * 返回百分比的值
         * @return
         */
        public int getCurrentCountpercent(){
            return (int)section;
        }

        /**
         * 设置当前的进度值以及进度条的颜色
         *
         * @param  mProgressBgColorId 进度值的颜色
         *
         *
         * */
        public void setCurrentCountColor(int mProgressBgColorId){

            switch (mProgressBgColorId) {
                case 0:
                    this.progressbarid = R.color.colorPrimary;//最大的橙色
                    break;
                case 1:
                    this.progressbarid = R.color.colorPrimaryDark;//其他的橙色
                    break;

                default:
                    break;
            }
        }


        /**
         * @param mProgressBgColorId
         */
        public void setCurrentCountColorIntegral(int mProgressBgColorId){

            this.progressbarid = mProgressBgColorId;//最大的橙色
        }

        /**
         * 设置进度条背景颜色
         * @param mProgressBgColorId
         */
        public void setprogressBackgroundColor(int mProgressBgColorId){

            this.backgroundid = mProgressBgColorId;//最大的橙色
        }




        public float getMaxCount() {
            return maxCount;
        }

        public float getCurrentCount() {
            return currentCount;
        }

        /* (non-Javadoc)
         * @see android.view.View#onMeasure(int, int)
         * 重写绘画方法，对progressView进行重新的绘制，貌似这个跟正常的使用并没有什么不同
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
                mWidth = widthSpecSize;
            } else {
                mWidth = 0;
            }
            if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
                mHeight = dipToPx(15);
            } else {
                mHeight = heightSpecSize;
            }
            setMeasuredDimension(mWidth, mHeight);
        }



    }







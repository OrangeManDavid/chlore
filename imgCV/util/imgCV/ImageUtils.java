package imgCV;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
//import android.support.annotation.ColorInt;
//import android.support.annotation.DrawableRes;
//import android.support.annotation.FloatRange;
//import android.support.annotation.IntRange;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/12
 *     desc  : utils about image
 * </pre>
 */
public final class ImageUtils {

	private ImageUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * Bitmap to bytes.
	 *
	 * @param bitmap The bitmap.
	 * @param format The format of bitmap.
	 * @return bytes
	 */
	public static byte[] bitmap2Bytes(final Bitmap bitmap, final CompressFormat format) {
		if (bitmap == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Bytes to bitmap.
	 *
	 * @param bytes The bytes.
	 * @return bitmap
	 */
	public static Bitmap bytes2Bitmap(final byte[] bytes) {
		return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * Drawable to bitmap.
	 *
	 * @param drawable The drawable.
	 * @return bitmap
	 */
	public static Bitmap drawable2Bitmap(final Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}
		Bitmap bitmap;
		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1,
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		}
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Drawable to bytes.
	 *
	 * @param drawable The drawable.
	 * @param format   The format of bitmap.
	 * @return bytes
	 */
	public static byte[] drawable2Bytes(final Drawable drawable, final CompressFormat format) {
		return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
	}

	/**
	 * View to bitmap.
	 *
	 * @param view The view.
	 * @return bitmap
	 */
	public static Bitmap view2Bitmap(final View view) {
		if (view == null)
			return null;
		boolean drawingCacheEnabled = view.isDrawingCacheEnabled();
		boolean willNotCacheDrawing = view.willNotCacheDrawing();
		view.setDrawingCacheEnabled(true);
		view.setWillNotCacheDrawing(false);
		final Bitmap drawingCache = view.getDrawingCache();
		Bitmap bitmap;
		if (null == drawingCache) {
			view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
					View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
			view.buildDrawingCache();
			bitmap = Bitmap.createBitmap(view.getDrawingCache());
		} else {
			bitmap = Bitmap.createBitmap(drawingCache);
		}
		view.destroyDrawingCache();
		view.setWillNotCacheDrawing(willNotCacheDrawing);
		view.setDrawingCacheEnabled(drawingCacheEnabled);
		return bitmap;
	}

	/**
	 * Return bitmap.
	 *
	 * @param file The file.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final File file) {
		if (file == null)
			return null;
		return BitmapFactory.decodeFile(file.getAbsolutePath());
	}

	/**
	 * Return bitmap.
	 *
	 * @param file      The file.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
		if (file == null)
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}

	/**
	 * Return bitmap.
	 *
	 * @param filePath The path of file.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final String filePath) {
		if (isSpace(filePath))
			return null;
		return BitmapFactory.decodeFile(filePath);
	}

	/**
	 * Return bitmap.
	 *
	 * @param filePath  The path of file.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {
		if (isSpace(filePath))
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * Return bitmap.
	 *
	 * @param is The input stream.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final InputStream is) {
		if (is == null)
			return null;
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * Return bitmap.
	 *
	 * @param is        The input stream.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final InputStream is, final int maxWidth, final int maxHeight) {
		if (is == null)
			return null;
		byte[] bytes = input2Byte(is);
		return getBitmap(bytes, 0, maxWidth, maxHeight);
	}

	/**
	 * Return bitmap.
	 *
	 * @param data   The data.
	 * @param offset The offset.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final byte[] data, final int offset) {
		if (data.length == 0)
			return null;
		return BitmapFactory.decodeByteArray(data, offset, data.length);
	}

	/**
	 * Return bitmap.
	 *
	 * @param data      The data.
	 * @param offset    The offset.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final byte[] data, final int offset, final int maxWidth, final int maxHeight) {
		if (data.length == 0)
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, offset, data.length, options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, offset, data.length, options);
	}

	/**
	 * Return bitmap.
	 *
	 * @param fd The file descriptor.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final FileDescriptor fd) {
		if (fd == null)
			return null;
		return BitmapFactory.decodeFileDescriptor(fd);
	}

	/**
	 * Return bitmap.
	 *
	 * @param fd        The file descriptor
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return bitmap
	 */
	public static Bitmap getBitmap(final FileDescriptor fd, final int maxWidth, final int maxHeight) {
		if (fd == null)
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(fd, null, options);
	}

	/**
	 * Return the bitmap with the specified color.
	 *
	 * @param src   The source of bitmap.
	 * @param color The color.
	 * @return the bitmap with the specified color
	 */

	/**
	 * Return the bitmap with the specified color.
	 *
	 * @param src     The source of bitmap.
	 * @param color   The color.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the bitmap with the specified color
	 */

	/**
	 * Return the scaled bitmap.
	 *
	 * @param src       The source of bitmap.
	 * @param newWidth  The new width.
	 * @param newHeight The new height.
	 * @return the scaled bitmap
	 */
	public static Bitmap scale(final Bitmap src, final int newWidth, final int newHeight) {
		return scale(src, newWidth, newHeight, false);
	}

	/**
	 * Return the scaled bitmap.
	 *
	 * @param src       The source of bitmap.
	 * @param newWidth  The new width.
	 * @param newHeight The new height.
	 * @param recycle   True to recycle the source of bitmap, false otherwise.
	 * @return the scaled bitmap
	 */
	public static Bitmap scale(final Bitmap src, final int newWidth, final int newHeight, final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the scaled bitmap
	 *
	 * @param src         The source of bitmap.
	 * @param scaleWidth  The scale of width.
	 * @param scaleHeight The scale of height.
	 * @return the scaled bitmap
	 */
	public static Bitmap scale(final Bitmap src, final float scaleWidth, final float scaleHeight) {
		return scale(src, scaleWidth, scaleHeight, false);
	}

	/**
	 * Return the scaled bitmap
	 *
	 * @param src         The source of bitmap.
	 * @param scaleWidth  The scale of width.
	 * @param scaleHeight The scale of height.
	 * @param recycle     True to recycle the source of bitmap, false otherwise.
	 * @return the scaled bitmap
	 */
	public static Bitmap scale(final Bitmap src, final float scaleWidth, final float scaleHeight,
			final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Matrix matrix = new Matrix();
		matrix.setScale(scaleWidth, scaleHeight);
		Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the clipped bitmap.
	 *
	 * @param src    The source of bitmap.
	 * @param x      The x coordinate of the first pixel.
	 * @param y      The y coordinate of the first pixel.
	 * @param width  The width.
	 * @param height The height.
	 * @return the clipped bitmap
	 */
	public static Bitmap clip(final Bitmap src, final int x, final int y, final int width, final int height) {
		return clip(src, x, y, width, height, false);
	}

	/**
	 * Return the clipped bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param x       The x coordinate of the first pixel.
	 * @param y       The y coordinate of the first pixel.
	 * @param width   The width.
	 * @param height  The height.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the clipped bitmap
	 */
	public static Bitmap clip(final Bitmap src, final int x, final int y, final int width, final int height,
			final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Bitmap ret = Bitmap.createBitmap(src, x, y, width, height);
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the skewed bitmap.
	 *
	 * @param src The source of bitmap.
	 * @param kx  The skew factor of x.
	 * @param ky  The skew factor of y.
	 * @return the skewed bitmap
	 */
	public static Bitmap skew(final Bitmap src, final float kx, final float ky) {
		return skew(src, kx, ky, 0, 0, false);
	}

	/**
	 * Return the skewed bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param kx      The skew factor of x.
	 * @param ky      The skew factor of y.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the skewed bitmap
	 */
	public static Bitmap skew(final Bitmap src, final float kx, final float ky, final boolean recycle) {
		return skew(src, kx, ky, 0, 0, recycle);
	}

	/**
	 * Return the skewed bitmap.
	 *
	 * @param src The source of bitmap.
	 * @param kx  The skew factor of x.
	 * @param ky  The skew factor of y.
	 * @param px  The x coordinate of the pivot point.
	 * @param py  The y coordinate of the pivot point.
	 * @return the skewed bitmap
	 */
	public static Bitmap skew(final Bitmap src, final float kx, final float ky, final float px, final float py) {
		return skew(src, kx, ky, px, py, false);
	}

	/**
	 * Return the skewed bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param kx      The skew factor of x.
	 * @param ky      The skew factor of y.
	 * @param px      The x coordinate of the pivot point.
	 * @param py      The y coordinate of the pivot point.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the skewed bitmap
	 */
	public static Bitmap skew(final Bitmap src, final float kx, final float ky, final float px, final float py,
			final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Matrix matrix = new Matrix();
		matrix.setSkew(kx, ky, px, py);
		Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the rotated bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param degrees The number of degrees.
	 * @param px      The x coordinate of the pivot point.
	 * @param py      The y coordinate of the pivot point.
	 * @return the rotated bitmap
	 */
	public static Bitmap rotate(final Bitmap src, final int degrees, final float px, final float py) {
		return rotate(src, degrees, px, py, false);
	}

	/**
	 * Return the rotated bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param degrees The number of degrees.
	 * @param px      The x coordinate of the pivot point.
	 * @param py      The y coordinate of the pivot point.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the rotated bitmap
	 */
	public static Bitmap rotate(final Bitmap src, final int degrees, final float px, final float py,
			final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		if (degrees == 0)
			return src;
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, px, py);
		Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the rotated degree.
	 *
	 * @param filePath The path of file.
	 * @return the rotated degree
	 */
	public static int getRotateDegree(final String filePath) {
		try {
			ExifInterface exifInterface = new ExifInterface(filePath);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			default:
				return 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Return the bitmap with reflection.
	 *
	 * @param src              The source of bitmap.
	 * @param reflectionHeight The height of reflection.
	 * @return the bitmap with reflection
	 */
	public static Bitmap addReflection(final Bitmap src, final int reflectionHeight) {
		return addReflection(src, reflectionHeight, false);
	}

	/**
	 * Return the bitmap with reflection.
	 *
	 * @param src              The source of bitmap.
	 * @param reflectionHeight The height of reflection.
	 * @param recycle          True to recycle the source of bitmap, false
	 *                         otherwise.
	 * @return the bitmap with reflection
	 */
	public static Bitmap addReflection(final Bitmap src, final int reflectionHeight, final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		final int REFLECTION_GAP = 0;
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight, srcWidth, reflectionHeight,
				matrix, false);
		Bitmap ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
		Canvas canvas = new Canvas(ret);
		canvas.drawBitmap(src, 0, 0, null);
		canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		LinearGradient shader = new LinearGradient(0, srcHeight, 0, ret.getHeight() + REFLECTION_GAP, 0x70FFFFFF,
				0x00FFFFFF, Shader.TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
		canvas.drawRect(0, srcHeight + REFLECTION_GAP, srcWidth, ret.getHeight(), paint);
		if (!reflectionBitmap.isRecycled())
			reflectionBitmap.recycle();
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the bitmap with image watermarking.
	 *
	 * @param src       The source of bitmap.
	 * @param watermark The image watermarking.
	 * @param x         The x coordinate of the first pixel.
	 * @param y         The y coordinate of the first pixel.
	 * @param alpha     The alpha of watermark.
	 * @return the bitmap with image watermarking
	 */
	public static Bitmap addImageWatermark(final Bitmap src, final Bitmap watermark, final int x, final int y,
			final int alpha) {
		return addImageWatermark(src, watermark, x, y, alpha, false);
	}

	/**
	 * Return the bitmap with image watermarking.
	 *
	 * @param src       The source of bitmap.
	 * @param watermark The image watermarking.
	 * @param x         The x coordinate of the first pixel.
	 * @param y         The y coordinate of the first pixel.
	 * @param alpha     The alpha of watermark.
	 * @param recycle   True to recycle the source of bitmap, false otherwise.
	 * @return the bitmap with image watermarking
	 */
	public static Bitmap addImageWatermark(final Bitmap src, final Bitmap watermark, final int x, final int y,
			final int alpha, final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Bitmap ret = src.copy(src.getConfig(), true);
		if (!isEmptyBitmap(watermark)) {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			Canvas canvas = new Canvas(ret);
			paint.setAlpha(alpha);
			canvas.drawBitmap(watermark, x, y, paint);
		}
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the alpha bitmap.
	 *
	 * @param src The source of bitmap.
	 * @return the alpha bitmap
	 */
	public static Bitmap toAlpha(final Bitmap src) {
		return toAlpha(src, false);
	}

	/**
	 * Return the alpha bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the alpha bitmap
	 */
	public static Bitmap toAlpha(final Bitmap src, final Boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Bitmap ret = src.extractAlpha();
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the gray bitmap.
	 *
	 * @param src The source of bitmap.
	 * @return the gray bitmap
	 */
	public static Bitmap toGray(final Bitmap src) {
		return toGray(src, false);
	}

	/**
	 * Return the gray bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the gray bitmap
	 */
	public static Bitmap toGray(final Bitmap src, final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		Bitmap ret = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		Canvas canvas = new Canvas(ret);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
		paint.setColorFilter(colorMatrixColorFilter);
		canvas.drawBitmap(src, 0, 0, paint);
		if (recycle && !src.isRecycled() && ret != src)
			src.recycle();
		return ret;
	}

	/**
	 * Return the blur bitmap using stack.
	 *
	 * @param src    The source of bitmap.
	 * @param radius The radius(0...25).
	 * @return the blur bitmap
	 */
	public static Bitmap stackBlur(final Bitmap src, final int radius) {
		return stackBlur(src, radius, false);
	}

	/**
	 * Return the blur bitmap using stack.
	 *
	 * @param src     The source of bitmap.
	 * @param radius  The radius(0...25).
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return the blur bitmap
	 */
	public static Bitmap stackBlur(final Bitmap src, int radius, final boolean recycle) {
		Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
		if (radius < 1) {
			radius = 1;
		}
		int w = ret.getWidth();
		int h = ret.getHeight();

		int[] pix = new int[w * h];
		ret.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}
		ret.setPixels(pix, 0, w, 0, 0, w, h);
		return ret;
	}

	/**
	 * Save the bitmap.
	 *
	 * @param src      The source of bitmap.
	 * @param filePath The path of file.
	 * @param format   The format of the image.
	 * @return {@code true}: success<br>
	 *         {@code false}: fail
	 */
	public static boolean save(final Bitmap src, final String filePath, final CompressFormat format) {
		return save(src, getFileByPath(filePath), format, false);
	}

	/**
	 * Save the bitmap.
	 *
	 * @param src    The source of bitmap.
	 * @param file   The file.
	 * @param format The format of the image.
	 * @return {@code true}: success<br>
	 *         {@code false}: fail
	 */
	public static boolean save(final Bitmap src, final File file, final CompressFormat format) {
		return save(src, file, format, false);
	}

	/**
	 * Save the bitmap.
	 *
	 * @param src      The source of bitmap.
	 * @param filePath The path of file.
	 * @param format   The format of the image.
	 * @param recycle  True to recycle the source of bitmap, false otherwise.
	 * @return {@code true}: success<br>
	 *         {@code false}: fail
	 */
	public static boolean save(final Bitmap src, final String filePath, final CompressFormat format,
			final boolean recycle) {
		return save(src, getFileByPath(filePath), format, recycle);
	}

	/**
	 * Save the bitmap.
	 *
	 * @param src     The source of bitmap.
	 * @param file    The file.
	 * @param format  The format of the image.
	 * @param recycle True to recycle the source of bitmap, false otherwise.
	 * @return {@code true}: success<br>
	 *         {@code false}: fail
	 */
	public static boolean save(final Bitmap src, final File file, final CompressFormat format, final boolean recycle) {
		if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file))
			return false;
		OutputStream os = null;
		boolean ret = false;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			ret = src.compress(format, 100, os);
			if (recycle && !src.isRecycled())
				src.recycle();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * Return whether it is a image according to the file name.
	 *
	 * @param file The file.
	 * @return {@code true}: yes<br>
	 *         {@code false}: no
	 */
	public static boolean isImage(final File file) {
		if (file == null || !file.exists()) {
			return false;
		}
		return isImage(file.getPath());
	}

	/**
	 * Return whether it is a image according to the file name.
	 *
	 * @param filePath The path of file.
	 * @return {@code true}: yes<br>
	 *         {@code false}: no
	 */
	public static boolean isImage(final String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
			return options.outWidth != -1 && options.outHeight != -1;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Return the type of image.
	 *
	 * @param filePath The path of file.
	 * @return the type of image
	 */
	public static ImageType getImageType(final String filePath) {
		return getImageType(getFileByPath(filePath));
	}

	/**
	 * Return the type of image.
	 *
	 * @param file The file.
	 * @return the type of image
	 */
	public static ImageType getImageType(final File file) {
		if (file == null)
			return null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			ImageType type = getImageType(is);
			if (type != null) {
				return type;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static ImageType getImageType(final InputStream is) {
		if (is == null)
			return null;
		try {
			byte[] bytes = new byte[12];
			return is.read(bytes) != -1 ? getImageType(bytes) : null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static ImageType getImageType(final byte[] bytes) {
		String type = bytes2HexString(bytes).toUpperCase();
		if (type.contains("FFD8FF")) {
			return ImageType.TYPE_JPG;
		} else if (type.contains("89504E47")) {
			return ImageType.TYPE_PNG;
		} else if (type.contains("47494638")) {
			return ImageType.TYPE_GIF;
		} else if (type.contains("49492A00") || type.contains("4D4D002A")) {
			return ImageType.TYPE_TIFF;
		} else if (type.contains("424D")) {
			return ImageType.TYPE_BMP;
		} else if (type.startsWith("52494646") && type.endsWith("57454250")) {// 524946461c57000057454250-12个字节
			return ImageType.TYPE_WEBP;
		} else if (type.contains("00000100") || type.contains("00000200")) {
			return ImageType.TYPE_ICO;
		} else {
			return ImageType.TYPE_UNKNOWN;
		}
	}

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	private static String bytes2HexString(final byte[] bytes) {
		if (bytes == null)
			return "";
		int len = bytes.length;
		if (len <= 0)
			return "";
		char[] ret = new char[len << 1];
		for (int i = 0, j = 0; i < len; i++) {
			ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
			ret[j++] = hexDigits[bytes[i] & 0x0f];
		}
		return new String(ret);
	}

	private static boolean isJPEG(final byte[] b) {
		return b.length >= 2 && (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(final byte[] b) {
		return b.length >= 6 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9')
				&& b[5] == 'a';
	}

	private static boolean isPNG(final byte[] b) {
		return b.length >= 8 && (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78 && b[3] == (byte) 71
				&& b[4] == (byte) 13 && b[5] == (byte) 10 && b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(final byte[] b) {
		return b.length >= 2 && (b[0] == 0x42) && (b[1] == 0x4d);
	}

	private static boolean isEmptyBitmap(final Bitmap src) {
		return src == null || src.getWidth() == 0 || src.getHeight() == 0;
	}

	///////////////////////////////////////////////////////////////////////////
	// about compress
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Return the compressed bitmap using scale.
	 *
	 * @param src       The source of bitmap.
	 * @param newWidth  The new width.
	 * @param newHeight The new height.
	 * @return the compressed bitmap
	 */
	public static Bitmap compressByScale(final Bitmap src, final int newWidth, final int newHeight) {
		return scale(src, newWidth, newHeight, false);
	}

	/**
	 * Return the compressed bitmap using scale.
	 *
	 * @param src       The source of bitmap.
	 * @param newWidth  The new width.
	 * @param newHeight The new height.
	 * @param recycle   True to recycle the source of bitmap, false otherwise.
	 * @return the compressed bitmap
	 */
	public static Bitmap compressByScale(final Bitmap src, final int newWidth, final int newHeight,
			final boolean recycle) {
		return scale(src, newWidth, newHeight, recycle);
	}

	/**
	 * Return the compressed bitmap using scale.
	 *
	 * @param src         The source of bitmap.
	 * @param scaleWidth  The scale of width.
	 * @param scaleHeight The scale of height.
	 * @return the compressed bitmap
	 */
	public static Bitmap compressByScale(final Bitmap src, final float scaleWidth, final float scaleHeight) {
		return scale(src, scaleWidth, scaleHeight, false);
	}

	/**
	 * Return the compressed bitmap using scale.
	 *
	 * @param src         The source of bitmap.
	 * @param scaleWidth  The scale of width.
	 * @param scaleHeight The scale of height.
	 * @param recycle     True to recycle the source of bitmap, false otherwise.
	 * @return he compressed bitmap
	 */
	public static Bitmap compressByScale(final Bitmap src, final float scaleWidth, final float scaleHeight,
			final boolean recycle) {
		return scale(src, scaleWidth, scaleHeight, recycle);
	}

	/**
	 * Return the compressed data using quality.
	 *
	 * @param src         The source of bitmap.
	 * @param maxByteSize The maximum size of byte.
	 * @return the compressed data using quality
	 */
	public static byte[] compressByQuality(final Bitmap src, final long maxByteSize) {
		return compressByQuality(src, maxByteSize, false);
	}

	/**
	 * Return the compressed data using quality.
	 *
	 * @param src         The source of bitmap.
	 * @param maxByteSize The maximum size of byte.
	 * @param recycle     True to recycle the source of bitmap, false otherwise.
	 * @return the compressed data using quality
	 */
	public static byte[] compressByQuality(final Bitmap src, final long maxByteSize, final boolean recycle) {
		if (isEmptyBitmap(src) || maxByteSize <= 0)
			return new byte[0];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		src.compress(CompressFormat.JPEG, 100, baos);
		byte[] bytes;
		if (baos.size() <= maxByteSize) {
			bytes = baos.toByteArray();
		} else {
			baos.reset();
			src.compress(CompressFormat.JPEG, 0, baos);
			if (baos.size() >= maxByteSize) {
				bytes = baos.toByteArray();
			} else {
				// find the best quality using binary search
				int st = 0;
				int end = 100;
				int mid = 0;
				while (st < end) {
					mid = (st + end) / 2;
					baos.reset();
					src.compress(CompressFormat.JPEG, mid, baos);
					int len = baos.size();
					if (len == maxByteSize) {
						break;
					} else if (len > maxByteSize) {
						end = mid - 1;
					} else {
						st = mid + 1;
					}
				}
				if (end == mid - 1) {
					baos.reset();
					src.compress(CompressFormat.JPEG, st, baos);
				}
				bytes = baos.toByteArray();
			}
		}
		if (recycle && !src.isRecycled())
			src.recycle();
		return bytes;
	}

	/**
	 * Return the compressed bitmap using sample size.
	 *
	 * @param src        The source of bitmap.
	 * @param sampleSize The sample size.
	 * @return the compressed bitmap
	 */

	public static Bitmap compressBySampleSize(final Bitmap src, final int sampleSize) {
		return compressBySampleSize(src, sampleSize, false);
	}

	/**
	 * Return the compressed bitmap using sample size.
	 *
	 * @param src        The source of bitmap.
	 * @param sampleSize The sample size.
	 * @param recycle    True to recycle the source of bitmap, false otherwise.
	 * @return the compressed bitmap
	 */
	public static Bitmap compressBySampleSize(final Bitmap src, final int sampleSize, final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bytes = baos.toByteArray();
		if (recycle && !src.isRecycled())
			src.recycle();
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}

	/**
	 * Return the compressed bitmap using sample size.
	 *
	 * @param src       The source of bitmap.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return the compressed bitmap
	 */
	public static Bitmap compressBySampleSize(final Bitmap src, final int maxWidth, final int maxHeight) {
		return compressBySampleSize(src, maxWidth, maxHeight, false);
	}

	/**
	 * Return the compressed bitmap using sample size.
	 *
	 * @param src       The source of bitmap.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @param recycle   True to recycle the source of bitmap, false otherwise.
	 * @return the compressed bitmap
	 */
	public static Bitmap compressBySampleSize(final Bitmap src, final int maxWidth, final int maxHeight,
			final boolean recycle) {
		if (isEmptyBitmap(src))
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bytes = baos.toByteArray();
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;
		if (recycle && !src.isRecycled())
			src.recycle();
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}

	/**
	 * Return the size of bitmap.
	 *
	 * @param filePath The path of file.
	 * @return the size of bitmap
	 */
	public static int[] getSize(String filePath) {
		return getSize(getFileByPath(filePath));
	}

	/**
	 * Return the size of bitmap.
	 *
	 * @param file The file.
	 * @return the size of bitmap
	 */
	public static int[] getSize(File file) {
		if (file == null)
			return new int[] { 0, 0 };
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		return new int[] { opts.outWidth, opts.outHeight };
	}

	/**
	 * Return the sample size.
	 *
	 * @param options   The options.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return the sample size
	 */
	public static int calculateInSampleSize(final BitmapFactory.Options options, final int maxWidth,
			final int maxHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		while (height > maxHeight || width > maxWidth) {
			height >>= 1;
			width >>= 1;
			inSampleSize <<= 1;
		}
		return inSampleSize;
	}

	///////////////////////////////////////////////////////////////////////////
	// other utils methods
	///////////////////////////////////////////////////////////////////////////

	private static File getFileByPath(final String filePath) {
		return isSpace(filePath) ? null : new File(filePath);
	}

	private static boolean createFileByDeleteOldFile(final File file) {
		if (file == null)
			return false;
		if (file.exists() && !file.delete())
			return false;
		if (!createOrExistsDir(file.getParentFile()))
			return false;
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean createOrExistsDir(final File file) {
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	private static boolean isSpace(final String s) {
		if (s == null)
			return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static byte[] input2Byte(final InputStream is) {
		if (is == null)
			return null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len;
			while ((len = is.read(b, 0, 1024)) != -1) {
				os.write(b, 0, len);
			}
			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public enum ImageType {
		TYPE_JPG("jpg"),

		TYPE_PNG("png"),

		TYPE_GIF("gif"),

		TYPE_TIFF("tiff"),

		TYPE_BMP("bmp"),

		TYPE_WEBP("webp"),

		TYPE_ICO("ico"),

		TYPE_UNKNOWN("unknown");

		String value;

		ImageType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}

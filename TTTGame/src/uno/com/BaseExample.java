package uno.com;

import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:10:28 - 11.04.2010
 */
public abstract class BaseExample extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	String path;
	private static final int MENU_TRACE = Menu.FIRST;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(Menu.NONE, MENU_TRACE, Menu.NONE, "Start Method Tracing");
		return super.onCreateOptionsMenu(pMenu);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu pMenu) {
		pMenu.findItem(MENU_TRACE).setTitle(
				this.mEngine.isMethodTracing() ? "Stop Method Tracing"
						: "Start Method Tracing");
		return super.onPrepareOptionsMenu(pMenu);
	}

	@Override
	public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
		switch (pItem.getItemId()) {
		case MENU_TRACE:
			if (this.mEngine.isMethodTracing()) {
				this.mEngine.stopMethodTracing();
			} else {
				this.mEngine.startMethodTracing("AndEngine_"
						+ System.currentTimeMillis() + ".trace");
			}
			return true;
		default:
			return super.onMenuItemSelected(pFeatureId, pItem);
		}
	}

	public void setPathResource(int w, int h) {
		switch (w) {
		case 240:
			switch (h) {
			case 320:
				BitmapTextureAtlasTextureRegionFactory
						.setAssetBasePath("gfx/ldpi/");
				path = "gfx/ldpi/";
				break;
			case 400:
				break;
			case 432:
				break;
			}
			break;
		case 320:
			BitmapTextureAtlasTextureRegionFactory
					.setAssetBasePath("gfx/mdpi/");
			path = "gfx/mdpi/";
			break;
		case 480:
			switch (h) {
			case 800:
				BitmapTextureAtlasTextureRegionFactory
						.setAssetBasePath("gfx/hdpi/");
				path = "gfx/hdpi/";
				break;
			case 854:
				BitmapTextureAtlasTextureRegionFactory
						.setAssetBasePath("gfx/hdpi-854x480/");
				path = "gfx/hdpi-854x480/";
				break;
			}
			break;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public BitmapTextureAtlas createBTA(String fileName, TextureOptions tO) {
		BitmapTextureAtlas bTA = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		InputStream in;
		try {
			in = this.getResources().getAssets().open(path + fileName);
			BitmapFactory.decodeStream(in, null, opt);
			int w = opt.outWidth;
			int h = opt.outHeight;
			int pw = 0;
			int ph = 0;
			boolean flag = MathUtils.isPowerOfTwo(w);
			if (!flag) {
				pw = MathUtils.nextPowerOfTwo(w);
			} else {
				pw = w;
			}
			flag = MathUtils.isPowerOfTwo(h);
			if (!flag) {
				ph = MathUtils.nextPowerOfTwo(h);
			} else {
				ph = h;
			}
			bTA = new BitmapTextureAtlas(pw, ph, tO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bTA;
	}

	public TextureRegion createTextureRegion(String fileName, TextureOptions tO) {
		TextureRegion tR = null;
		BitmapTextureAtlas bTA = createBTA(fileName, tO);
		tR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bTA, this,
				fileName, 0, 0);
		this.mEngine.getTextureManager().loadTexture(bTA);
		return tR;
	}
	
	public TiledTextureRegion createTileTextureRegion(String fileName,
			TextureOptions tO,int pTileColumns,int pTileRows) {
		TiledTextureRegion tTR = null;
		BitmapTextureAtlas bTA = createBTA(fileName, tO);
		tTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bTA,
				this, fileName, 0, 0, pTileColumns, pTileRows);
		this.mEngine.getTextureManager().loadTexture(bTA);
		return tTR;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

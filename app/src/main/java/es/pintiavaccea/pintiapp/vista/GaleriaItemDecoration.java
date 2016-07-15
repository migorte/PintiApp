package es.pintiavaccea.pintiapp.vista;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Miguel on 09/06/2016.
 *
 * Define la decoración de los elementos de la galería de imágenes.
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class GaleriaItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public GaleriaItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}

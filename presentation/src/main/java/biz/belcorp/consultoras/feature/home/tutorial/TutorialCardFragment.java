package biz.belcorp.consultoras.feature.home.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.tutorial.TutorialModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author andres.escobar on 3/08/2017.
 */
public class TutorialCardFragment extends BaseFragment {

    @BindView(R.id.ivw_tutorial_imagen)
    ImageView ivwTutorialImagen;
    @BindView(R.id.tvw_tutorial_title_1)
    TextView tvwTutorialTitle1;
    @BindView(R.id.tvw_tutorial_title_2)
    TextView tvwTutorialTitle2;
    @BindView(R.id.tvw_tutorial_title_3)
    TextView tvwTutorialTitle3;
    @BindView(R.id.tvw_tutorial_description)
    TextView tvwTutorialDescription;

    private Unbinder unbinder;
    private TutorialModel tutorialModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_card, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getTutorialModel() == null) return;

        tvwTutorialTitle1.setText(getTutorialModel().getTitle1());
        tvwTutorialTitle2.setText(getTutorialModel().getTitle2());
        tvwTutorialTitle3.setText(getTutorialModel().getTitle3());
        tvwTutorialDescription.setText(getTutorialModel().getDescripcion());

        Glide.with(this).load(getTutorialModel().getImagen()).into(ivwTutorialImagen);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }

    /**********************************************************************************************/

    @Override
    public Context context() {
        return getContext();
    }

    public TutorialModel getTutorialModel() {
        return tutorialModel;
    }

    public void setTutorialModel(TutorialModel tutorialModel) {
        this.tutorialModel = tutorialModel;
    }
}

package com.digitalflow.belchior.appbelchior.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.MusicActivity;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Fragments.MusicFragment;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarllonS on 03/03/2018.
 */


public class MusicAdapter extends ArrayAdapter<Musicas> {

    private Context context, cocontext;
    private ArrayList<Musicas> musics;

    public MusicAdapter(@NonNull Context c, @NonNull ArrayList<Musicas> musics) {
        super(c, 0, musics);
        this.context = c;
        this.musics = musics;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = null;
        if (view == null) { //se a view existe não cria novamente
            //Inicializa objeto
            inflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            cocontext = parent.getContext();
            // /monta o xml pra popular a lista
            view = inflater.inflate(R.layout.list_music, parent, false);
        }

        //verificar se foi criado elementos
        if (true) {
            //recuperar dados na tela
            TextView textViewValue = (TextView) view.findViewById(R.id.textViewValue);
            Button btnPlayMusic = (Button) view.findViewById(R.id.btnPlayMusic);
            textViewValue.setText(returnTitleMusic(musics.get(position).getNome()));
            setImageButton(((Boolean) musics.get(position).getLocked()).booleanValue(), btnPlayMusic, inflater);
        } else {
            //nao foi criado elementos da view
        }
        return view;
    }

    public String returnTitleMusic(String rootMusic) {
        switch (rootMusic) {
            case "Music 0":
                return Crud.titleMusics[0];
            case "Music 1":
                return Crud.titleMusics[1];
            case "Music 2":
                return Crud.titleMusics[2];
            case "Music 3":
                return Crud.titleMusics[3];
            case "Music 4":
                return Crud.titleMusics[4];
            case "Music 5":
                return Crud.titleMusics[5];
            case "Music 6":
                return Crud.titleMusics[6];
            case "Music 7":
                return Crud.titleMusics[7];
            case "Music 8":
                return Crud.titleMusics[8];
            case "Music 9":
                return Crud.titleMusics[9];
            default:
                return "";
        }
    }

    public void setImageButton(Boolean switcher, Button btn, final LayoutInflater inflater) {
        if ((switcher.toString() == "true") || (switcher.toString() == "false")) {
            if (switcher) {
                btn.setBackgroundResource(R.drawable.botao_desbloqueado);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View parentRow = (View) v.getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        final int position = listView.getPositionForView(parentRow);
                        HelperAux helper = new HelperAux();
                        helper.AlertDialogMusic(listView.getContext(), inflater, Crud.titleMusics[position], position);
//                        Toast.makeText(context, "reproduzir musica "+ position, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                btn.setBackgroundResource(R.drawable.botao_bloqueado);
            }
        } else {
            btn.setBackgroundResource(R.color.transparent);
        }

    }




}

package com.digitalflow.belchior.appbelchior.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarllonS on 03/03/2018.
 */

public class MusicAdapter extends ArrayAdapter<Musicas> {

    private Context context;
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

        if(view == null){ //se a view existe não cria novamente
            //Inicializa objeto
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta o xml pra popular a lista
            view = inflater.inflate(R.layout.list_music, parent, false);
        }

        //verificar algo que nao sei ainda
        if (true) {
            //recuperar dados na tela
            TextView textViewBool = (TextView) view.findViewById(R.id.textViewBool);
            TextView textViewValue = (TextView) view.findViewById(R.id.textViewValue);
            textViewBool.setText(musics.get(position).getNome());
            textViewValue.setText(musics.get(position).getLocked().toString());


        }

        return view;
    }
}

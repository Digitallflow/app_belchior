package com.digitalflow.belchior.appbelchior.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        if (view == null) { //se a view existe não cria novamente
            //Inicializa objeto
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            //monta o xml pra popular a lista
            view = inflater.inflate(R.layout.list_music, parent, false);
        }

        //verificar se foi criado elementos
        if (true) {
            //recuperar dados na tela
            TextView textViewValue = (TextView) view.findViewById(R.id.textViewValue);
            Button buttonBool = (Button) view.findViewById(R.id.buttonBool);
            textViewValue.setText(returnTitleMusic(musics.get(position).getNome()));
            setImageButton(((Boolean) musics.get(position).getLocked()).booleanValue(), buttonBool, position);
        } else {
            //nao foi criado elementos da view
        }
        return view;
    }

    public String returnTitleMusic(String rootMusic) {
        switch (rootMusic) {
            case "Music 0":
                return "Fotografia 3x4 (1976)";
            case "Music 1":
                return "Tudo Outra Vez (1979)";
            case "Music 2":
                return "Galos, Noites e Quintais (1977)";
            case "Music 3":
                return "A Palo Seco (1973)";
            case "Music 4":
                return "Alucinação (1976)";
            case "Music 5":
                return "Divina Comédia Romântica (1978)";
            case "Music 6":
                return "Coração Selvagem (1977)";
            case "Music 7":
                return "Velha Roupa Colorida (1976)";
            case "Music 8":
                return "Como Nossos Pais (1976)";
            case "Music 9":
                return "Paralelas (1976)";
            default:
                return "";
        }
    }

    public void setImageButton(Boolean switcher, Button btn, int position) {
        if ((switcher.toString() == "true") || (switcher.toString() == "false")) {
            if (switcher) {
                btn.setBackgroundResource(R.drawable.botao_desbloqueado);
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "reproduzir musica", Toast.LENGTH_LONG).show();
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

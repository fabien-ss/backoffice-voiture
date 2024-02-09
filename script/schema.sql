create table favorite_annonce(
    id_annonce varchar(50) references annonce(id_annonce),
    id_utilisateur varchar(50) references utilisateur(id_utilisateur)
    state_value int references favorite_state(value) 
);

create table favorite_state(
    value int UNIQUE,
    state varchar(50)
);
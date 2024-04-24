
create sequence centro_seq start with 1 increment by 50;
create sequence gerente_seq start with 1 increment by 50;
create sequence mensaje_seq start with 1 increment by 50;
create table centro (id_centro integer not null, direccion varchar(255), nombre varchar(255), primary key (id_centro));
create table gerente (centro_id_centro integer unique, id integer not null, id_usuario integer, empresa varchar(255), primary key (id));
create table mensaje (id integer, id_mensaje integer not null, asunto varchar(255), contenido varchar(255), primary key (id_mensaje));
create table mensaje_copia (id integer, mensaje_id_mensaje integer not null);
create table mensaje_copia_oculta (id integer, mensaje_id_mensaje integer not null);
create table mensaje_destinatarios (id integer, mensaje_id_mensaje integer not null);
alter table if exists gerente add constraint FK20m0qlh8u9369rd0nj0vahx9g foreign key (centro_id_centro) references centro;
alter table if exists mensaje_copia add constraint FKifqsn5uq978dknr86x5hmbg9 foreign key (mensaje_id_mensaje) references mensaje;
alter table if exists mensaje_copia_oculta add constraint FKk54r5a92apsmjww1g44mchxgu foreign key (mensaje_id_mensaje) references mensaje;
alter table if exists mensaje_destinatarios add constraint FKm2185he9xxbw5afdn5v898vil foreign key (mensaje_id_mensaje) references mensaje;

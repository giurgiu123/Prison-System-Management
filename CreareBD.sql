CREATE TABLE `Detinut`(
    `id_detinut` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `nume` VARCHAR(255) NOT NULL,
    `fk_id_celula` INT UNSIGNED NOT NULL,
    `fk_id_utilizator` INT UNSIGNED NOT NULL,
    `profesie` VARCHAR(255) NOT NULL
);
CREATE TABLE `Bloc`(
    `id_bloc` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `descriere_bloc` VARCHAR(255) NOT NULL
);
CREATE TABLE `Celula`(
    `id_celula` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fk_id_etaj` TINYINT UNSIGNED NOT NULL,
    `fk_id_gardian` INT UNSIGNED NOT NULL,
    `locuri_ramase` TINYINT NOT NULL
);
CREATE TABLE `Gardian`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fk_id_shift` TINYINT UNSIGNED NOT NULL,
    `fk_id_utilizator` INT UNSIGNED NOT NULL
);
CREATE TABLE `shift`(
    `id_shift` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fk_id_etaj` TINYINT UNSIGNED NOT NULL,
    `tip_shift` TINYINT UNSIGNED NOT NULL,
    `zi` TINYINT UNSIGNED NOT NULL
);
CREATE TABLE `Carcera`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fk_id_detinut` INT UNSIGNED NOT NULL,
    `end_time` DATETIME NOT NULL
);
CREATE TABLE `sentinta`(
    `id_sentita` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `categorie` VARCHAR(255) NOT NULL,
    `motiv_specific` VARCHAR(255) NOT NULL,
    `end_time` DATETIME NOT NULL,
    `fk_id_detinut` INT UNSIGNED NOT NULL,
    `start_time` DATETIME NOT NULL
);
CREATE TABLE `vizitator`(
    `id_vizitator` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `nume` VARCHAR(255) NOT NULL,
    `fk_id_programare` INT UNSIGNED NOT NULL,
    `fk_id_utilizator` INT UNSIGNED NOT NULL
);
CREATE TABLE `Programari`(
    `id_programare` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `tip_programari` TINYINT NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `fk_id_prizioner` INT UNSIGNED NOT NULL
);
CREATE TABLE `Task_inchisoare`(
    `id_task` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `difficulty` TINYINT NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `description` VARCHAR(255) NOT NULL
);
CREATE TABLE `Inscriere_task`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fk_id_detinut` INT UNSIGNED NOT NULL,
    `fk_id_task` INT UNSIGNED NOT NULL
);
CREATE TABLE `Utilizator`(
    `id_utilizator` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `drept_access` TINYINT NOT NULL COMMENT '0->warden
1->gardian
2->vizitator'
);
CREATE TABLE `etaj`(
    `id_etaj` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fk_id_bloc` TINYINT UNSIGNED NOT NULL,
    `nr_etaj` TINYINT NOT NULL
);
ALTER TABLE
    `Inscriere_task` ADD CONSTRAINT `inscriere_task_fk_id_task_foreign` FOREIGN KEY(`fk_id_task`) REFERENCES `Task_inchisoare`(`id_task`);
ALTER TABLE
    `Celula` ADD CONSTRAINT `celula_fk_id_gardian_foreign` FOREIGN KEY(`fk_id_gardian`) REFERENCES `Gardian`(`id`);
ALTER TABLE
    `etaj` ADD CONSTRAINT `etaj_fk_id_bloc_foreign` FOREIGN KEY(`fk_id_bloc`) REFERENCES `Bloc`(`id_bloc`);
ALTER TABLE
    `Inscriere_task` ADD CONSTRAINT `inscriere_task_fk_id_detinut_foreign` FOREIGN KEY(`fk_id_detinut`) REFERENCES `Detinut`(`id_detinut`);
ALTER TABLE
    `Detinut` ADD CONSTRAINT `detinut_fk_id_utilizator_foreign` FOREIGN KEY(`fk_id_utilizator`) REFERENCES `Utilizator`(`id_utilizator`);
ALTER TABLE
    `Celula` ADD CONSTRAINT `celula_fk_id_etaj_foreign` FOREIGN KEY(`fk_id_etaj`) REFERENCES `etaj`(`id_etaj`);
ALTER TABLE
    `Gardian` ADD CONSTRAINT `gardian_fk_id_utilizator_foreign` FOREIGN KEY(`fk_id_utilizator`) REFERENCES `Utilizator`(`id_utilizator`);
ALTER TABLE
    `Detinut` ADD CONSTRAINT `detinut_fk_id_celula_foreign` FOREIGN KEY(`fk_id_celula`) REFERENCES `Celula`(`id_celula`);
ALTER TABLE
    `Carcera` ADD CONSTRAINT `carcera_fk_id_detinut_foreign` FOREIGN KEY(`fk_id_detinut`) REFERENCES `Detinut`(`id_detinut`);
ALTER TABLE
    `vizitator` ADD CONSTRAINT `vizitator_fk_id_programare_foreign` FOREIGN KEY(`fk_id_programare`) REFERENCES `Programari`(`id_programare`);
ALTER TABLE
    `Programari` ADD CONSTRAINT `programari_fk_id_prizioner_foreign` FOREIGN KEY(`fk_id_prizioner`) REFERENCES `Detinut`(`id_detinut`);
ALTER TABLE
    `sentinta` ADD CONSTRAINT `sentinta_fk_id_detinut_foreign` FOREIGN KEY(`fk_id_detinut`) REFERENCES `Detinut`(`id_detinut`);
ALTER TABLE
    `vizitator` ADD CONSTRAINT `vizitator_fk_id_utilizator_foreign` FOREIGN KEY(`fk_id_utilizator`) REFERENCES `Utilizator`(`id_utilizator`);
ALTER TABLE
    `shift` ADD CONSTRAINT `shift_fk_id_etaj_foreign` FOREIGN KEY(`fk_id_etaj`) REFERENCES `etaj`(`id_etaj`);
ALTER TABLE
    `Gardian` ADD CONSTRAINT `gardian_fk_id_shift_foreign` FOREIGN KEY(`fk_id_shift`) REFERENCES `shift`(`id_shift`);

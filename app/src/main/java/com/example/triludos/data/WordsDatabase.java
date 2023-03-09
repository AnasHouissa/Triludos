package com.example.triludos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Database(entities = {WordEntity.class}, version = 1)
public abstract class WordsDatabase extends RoomDatabase {
    private static final String DB_NAME = "Hangman_words_db";
    private static WordsDatabase instance;

    public static synchronized WordsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.
                    databaseBuilder(context.getApplicationContext(), WordsDatabase.class, DB_NAME).
                    fallbackToDestructiveMigration().
                    allowMainThreadQueries().
                    addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            List<WordEntity> words = new ArrayList<>();
                            words = createWords();
                            db.beginTransaction();

                            for (WordEntity word : words) {
                                ContentValues values = new ContentValues();
                                values.put("word", word.getWord());
                                values.put("hint", word.getHint());
                                values.put("lang", word.getLang());
                                db.insert("WordEntity", SQLiteDatabase.CONFLICT_IGNORE, values);
                            }
                            db.setTransactionSuccessful();
                            db.endTransaction();
                        }
                    }).
                    build();
        }
        return instance;
    }

    public abstract WordDAO wordDAO();

    public static List<WordEntity> createWords() {
        List<WordEntity> words = new ArrayList<>();
        /** English Version **/
        WordEntity wordEn1 = new WordEntity("FOOTBALL", "A sport in which teams attempt to get a ball into a goal or zone defended by the other team.", "en");
        WordEntity wordEn2 = new WordEntity("SCHOOL", "A learning facility", "en");
        WordEntity wordEn3 = new WordEntity("CAT", "A small animal related to lions.", "en");
        WordEntity wordEn4 = new WordEntity("CROCODILE", "A large reptile that lives in the water.", "en");
        WordEntity wordEn5 = new WordEntity("BRIDGE", "A structure built over a river.", "en");
        WordEntity wordEn6 = new WordEntity("CAMERA", "Cheese catchers.", "en");
        WordEntity wordEn7 = new WordEntity("HANGMAN", "It's me.", "en");
        WordEntity wordEn8 = new WordEntity("CUCUMBER", "A delicious cylindrical green item.", "en");
        WordEntity wordEn9 = new WordEntity("PARIS", "City of Light.", "en");
        WordEntity wordEn10 = new WordEntity("PRINTER", "An output device that consume papers.", "en");
        WordEntity wordEn11 = new WordEntity("GATE", "An entrance.", "en");
        WordEntity wordEn12 = new WordEntity("LAPTOP", "A portable machine.", "en");
        WordEntity wordEn13 = new WordEntity((android.os.Build.MANUFACTURER).toUpperCase(), "Device's manufacturer.", "en");
        WordEntity wordEn14 = new WordEntity("CIRCUMSTANCES", "Everything that happens to one in life.", "en");
        WordEntity wordEn15 = new WordEntity("ENCYCLOPEDIA", "A reference work containing articles on various topics dealing with the human knowledge.", "en");
        WordEntity wordEn16 = new WordEntity("WATER", "A liquid.", "en");
        WordEntity wordEn17 = new WordEntity("NECKLACE", "A jewelry.", "en");
        WordEntity wordEn18 = new WordEntity("LIBRARY", "A building that houses a collection of books.", "en");
        WordEntity wordEn19 = new WordEntity("HYPHEN", "A punctuation mark", "en");
        WordEntity wordEn20 = new WordEntity("ZIGZAG", "An angular shape characterized by sharp turns.", "en");
        WordEntity wordEn21 = new WordEntity("UNKNOWN", "Not identified.", "en");
        WordEntity wordEn22 = new WordEntity("GRAVITY", "The force of attraction between all masses in the universe.", "en");
        WordEntity wordEn23 = new WordEntity("CHEMICAL", "Relating to or used in chemistry.", "en");
        WordEntity wordEn24 = new WordEntity("COUNTRY", "An area outside of cities and towns.", "en");
        WordEntity wordEn25 = new WordEntity("SLOVAKIA", "A landlocked republic in central Europe, separated from the Czech Republic.", "en");
        WordEntity wordEn26 = new WordEntity("CANADA", "A nation in northern North America.", "en");
        WordEntity wordEn27 = new WordEntity("GREECE", "Plato's country.", "en");
        WordEntity wordEn28 = new WordEntity("WOODS", "Collections of trees, closely packed.", "en");
        WordEntity wordEn29 = new WordEntity("THERMOMETER", "Measuring instrument.", "en");
        WordEntity wordEn30 = new WordEntity("MERCURY", "A heavy silvery toxic univalent and bivalent metallic element.", "en");
        WordEntity wordEn31 = new WordEntity("SCORE", "A number that expresses the accomplishment of a team.", "en");
        WordEntity wordEn32 = new WordEntity("HUSBAND", "A woman's partner in marriage.", "en");
        WordEntity wordEn33 = new WordEntity("INCOMPATIBLE", "Incapable of blending into a stable homogeneous mixture.", "en");
        WordEntity wordEn34 = new WordEntity("NEGATIVE", "Expressing a denial or refusal.", "en");
        WordEntity wordEn35 = new WordEntity("PHOTOCOPY", "Reproduce on paper.", "en");
        WordEntity wordEn36 = new WordEntity("PHOTOGRAPH", "A representation of a person or scene in the form of a print.", "en");
        WordEntity wordEn37 = new WordEntity("DUPLICATE", "A copy.", "en");
        WordEntity wordEn38 = new WordEntity("EQUIVALENT", "A person or thing equal to another in value or measure.", "en");
        WordEntity wordEn39 = new WordEntity("REPRESENTATION", "A visual creation.", "en");
        WordEntity wordEn41 = new WordEntity("ASSIGNMENT", "A job someone is given to do.", "en");
        WordEntity wordEn42 = new WordEntity("MISSION", "Specific task or duty.", "en");
        WordEntity wordEn43 = new WordEntity("AIM", "Order between \"ready\" and \"fire\".", "en");
        WordEntity wordEn44 = new WordEntity("EMERGENCY", "\"Help!\".", "en");
        WordEntity wordEn45 = new WordEntity("LABORATORY", "Place of scientific research.", "en");
        WordEntity wordEn46 = new WordEntity("PHARMACY", "Chemist shop.", "en");
        WordEntity wordEn47 = new WordEntity("FLORIST", "A shop where flowers and ornamental plants are sold.", "en");
        WordEntity wordEn48 = new WordEntity("ALIEN", "Visitor from another world.", "en");
        WordEntity wordEn49 = new WordEntity("ILLEGAL", "Not lawful.", "en");
        WordEntity wordEn50 = new WordEntity("INCORRECT", "Not in conformity with fact or truth.", "en");


        /** French Version **/

        WordEntity wordFr1 = new WordEntity("FOOTBALL", "Un sport collectif.", "fr");
        WordEntity wordFr2 = new WordEntity("ECOLE", "Un lieu dédié à l’apprentissage..", "fr");
        WordEntity wordFr3 = new WordEntity("CHAT", "Amateur de souris.", "fr");
        WordEntity wordFr4 = new WordEntity("CROCODILE", "Un grand reptile qui vit dans l'eau.", "fr");
        WordEntity wordFr5 = new WordEntity("PONT", "Construction qui relie les deux rives d'un cours d'eau.", "en");
        WordEntity wordFr6 = new WordEntity("CAMERA", "Appareil permettant de filmer, de réaliser la prise de vues d’un film.", "fr");
        WordEntity wordFr7 = new WordEntity("HANGMAN", "C'est moi.", "fr");
        WordEntity wordFr8 = new WordEntity("CONCOMBRE", "Bon en salade.", "fr");
        WordEntity wordFr9 = new WordEntity("PARIS", "Ville lumière.", "fr");
        WordEntity wordFr10 = new WordEntity("IMPRIMANTE", "Périphérique informatique destiné à imprimer du texte ou des éléments graphiques sur du papier.", "fr");
        WordEntity wordFr11 = new WordEntity("PORTAIL", "Point d'entrée d'un parc ou d'une résidence.", "fr");
        WordEntity wordFr13 = new WordEntity((android.os.Build.MANUFACTURER).toUpperCase(), "Fabricant de l'appareil.", "fr");
        WordEntity wordFr14 = new WordEntity("CIRCONSTANCES", "Particularité qui accompagne et distingue un fait, une situation, etc.", "fr");
        WordEntity wordFr15 = new WordEntity("ENCYCLOPEDIE", "Ouvrage qui traite systématiquement d'un ou plusieurs domaines des connaissances.", "fr");
        WordEntity wordFr16 = new WordEntity("EAU", "Un liquide.", "fr");
        WordEntity wordFr17 = new WordEntity("COLLIER", "Un bijou.", "fr");
        WordEntity wordFr18 = new WordEntity("BIBLIOTHEQUE", "Salle où sont conservés des livres.", "fr");
        WordEntity wordFr20 = new WordEntity("ZIGZAG", "Changements fréquents de direction.", "fr");
        WordEntity wordFr21 = new WordEntity("INCONNUE", "Personne que l’on ne connaît pas.", "fr");
        WordEntity wordFr22 = new WordEntity("GRAVITE", "Elle nous fait garder les pieds sur terre.", "fr");
        WordEntity wordFr23 = new WordEntity("CHIMIQUE", "Contraire de naturel, pour un produit.", "fr");
        WordEntity wordFr24 = new WordEntity("PAYS", "Territoire d’une nation.", "fr");
        WordEntity wordFr25 = new WordEntity("SLOVAQUIE", "Bratislava est sa capitale.", "fr");
        WordEntity wordFr26 = new WordEntity("CANADA", "Pays de québec.", "fr");
        WordEntity wordFr27 = new WordEntity("GRECE", "Sa capitale est Athènes.", "frfr");
        WordEntity wordFr28 = new WordEntity("BOIS", "Ensemble d’arbres assez proches.", "fr");
        WordEntity wordFr29 = new WordEntity("THERMOMETRE", "Instrument de mesure.", "fr");
        WordEntity wordFr30 = new WordEntity("MERCURE", "Métal de couleur argent brillant.", "fr");
        WordEntity wordFr31 = new WordEntity("SCORE", "Nombre de points qu’un joueur, une équipe a marqué.", "fr");
        WordEntity wordFr32 = new WordEntity("MARI", "Homme de couple.", "fr");
        WordEntity wordFr33 = new WordEntity("INCOMPATIBLE", "Qui n’est pas compatible.", "fr");
        WordEntity wordFr34 = new WordEntity("NEGATIVE", "Non.", "fr");
        WordEntity wordFr35 = new WordEntity("PHOTOCOPIE", "Copie à l’aide d’une photocopieuse.", "fr");
        WordEntity wordFr36 = new WordEntity("PHOTOGRAPHIER", "Obtenir une image", "fr");
        WordEntity wordFr37 = new WordEntity("DUPLIQUER", "Faire un double de quelque chose.", "fr");
        WordEntity wordFr38 = new WordEntity("EQUIVALENTE", "Qui est de même valeur.", "fr");
        WordEntity wordFr39 = new WordEntity("REPRESENTATION", "Idée qu'on se fait d'un objet particulier.", "fr");
        WordEntity wordFr40 = new WordEntity("INDISCERNABLE", "Qui ne peut être distingué d’une chose de même nature.", "fr");
        WordEntity wordFr41 = new WordEntity("TACHE", "Travail donné à accomplir.", "fr");
        WordEntity wordFr42 = new WordEntity("MISSION", "Elle semble toujours Impossible pour Tom Cruise.", "fr");
        WordEntity wordFr43 = new WordEntity("BUT", "Point que l’on vise.", "fr");
        WordEntity wordFr44 = new WordEntity("URGENCE", "Caractère de ce qui ne peut pas attendre.", "fr");
        WordEntity wordFr45 = new WordEntity("LABORATOIRE", "Local où l'on fait de la recherche scientifique.", "fr");
        WordEntity wordFr46 = new WordEntity("PHARMACIE", "La science des remèdes et des médicaments.", "fr");
        WordEntity wordFr47 = new WordEntity("FLEURISTE", "Il vend beaucoup de bouquets à la Saint-Valentin.", "fr");
        WordEntity wordFr48 = new WordEntity("EXTRATERRESTRE", "Créature étrange.", "fr");
        WordEntity wordFr49 = new WordEntity("ILLEGALE", "Contraire à la loi.", "fr");
        WordEntity wordFr50 = new WordEntity("INCORRECT", "Qui n’est pas correct.", "fr");
        Collections.addAll(words, wordEn1,
                wordEn2,
                wordEn3,
                wordEn4,
                wordEn5,
                wordEn6,
                wordEn7,
                wordEn8,
                wordEn9,
                wordEn10,
                wordEn11,
                wordEn12,
                wordEn13,
                wordEn14,
                wordEn15,
                wordEn16,
                wordEn17,
                wordEn18,
                wordEn19,
                wordEn20,
                wordEn21,
                wordEn22,
                wordEn23,
                wordEn24,
                wordEn25,
                wordEn26,
                wordEn27,
                wordEn28,
                wordEn29,
                wordEn30,
                wordEn31,
                wordEn32,
                wordEn33,
                wordEn34,
                wordEn35,
                wordEn36,
                wordEn37,
                wordEn38,
                wordEn39,
                wordEn41,
                wordEn42,
                wordEn43,
                wordEn44,
                wordEn45,
                wordEn46,
                wordEn47,
                wordEn48,
                wordEn49,
                wordEn50,
                wordFr1,
                wordFr2,
                wordFr3,
                wordFr4,
                wordFr5,
                wordFr6,
                wordFr7,
                wordFr8,
                wordFr9,
                wordFr10,
                wordFr11,
                wordFr13,
                wordFr14,
                wordFr15,
                wordFr16,
                wordFr17,
                wordFr18,
                wordFr20,
                wordFr21,
                wordFr22,
                wordFr23,
                wordFr24,
                wordFr25,
                wordFr26,
                wordFr27,
                wordFr28,
                wordFr29,
                wordFr30,
                wordFr31,
                wordFr32,
                wordFr33,
                wordFr34,
                wordFr35,
                wordFr36,
                wordFr37,
                wordFr38,
                wordFr39,
                wordFr40,
                wordFr41,
                wordFr42,
                wordFr43,
                wordFr44,
                wordFr45,
                wordFr46,
                wordFr47,
                wordFr48,
                wordFr49,
                wordFr50
        );
        return words;
    }
}

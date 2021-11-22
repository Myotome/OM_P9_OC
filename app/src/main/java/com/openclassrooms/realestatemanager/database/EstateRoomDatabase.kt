package com.openclassrooms.realestatemanager.database


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.di.ApplicationScope
import com.openclassrooms.realestatemanager.model.*
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Estate::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class EstateRoomDatabase : RoomDatabase() {

    abstract fun EstateDAO(): EstateDAO

    class Callback @Inject constructor(
        private val database: Provider<EstateRoomDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            val dao = database.get().EstateDAO()
//
//            val listPhoto = ArrayList<Photo>()
//            listPhoto.add(Photo(image = "content://media/external/images/media/162", name = "view"))
//            listPhoto.add(Photo(image = "content://media/external/images/media/158", name = "view"))
//            listPhoto.add(
//                Photo(
//                    image = "content://media/external/images/media/159",
//                    name = "swimming pool"
//                )
//            )
//
//
//            applicationScope.launch {
//                dao.insertEstate(
//                    Estate(
//                        primaryEstateData = PrimaryEstateData(
//                            estateType = "Penthouse",
//                            price = 21_224_700,
//                            surface = 313.0,
//                            rooms = 6,
//                            landSize = null,
//                            soldDate = null
//                        ),
//                        secondaryEstateData = SecondaryEstateData(
//                            firebaseId = 87946516263854,
//                            bedroom = 3,
//                            bathroom = 3,
//                            description = "Massive maison Demi-étage Penthouse avec 176 carrés terrasse au pied dispose d'un mur de fenêtres en verre incurvées offrant une vue panoramique imprenable sur la rivière Hudson, NY Harbor, Statue de la Liberté, World Trade Center et Manhattan Skyline. La série L, une collection de 50 West Streets penthouse le plus remarquable, pleine et résidences demi-de-chaussée, offre un éventail impressionnant de fonctionnalités supplémentaires. Les cuisines, aménagées avec des comptoirs de dalles en pierre et dosserets, sont équipées d'un réfrigérateur extra-spacieuses et congélateurs, un poêle à six brûleurs, et un réfrigérateur à vin pleine hauteur. Grandes dalles de pierre de marbre, une baignoire autoportante trempage, une douche à vapeur et un sauna benched améliorer l'élégante salle de bains principale. Ouest, une tour résidentielle de 64 étages situé dans le centre du New Downtown, offre une vue imprenable sur le port de New York, l'Hudson et East Rivers, la Statue de la Liberté et Ellis Island. architecte de renommée internationale Helmut Jahn a conçu le environ 780 'gratte-ciel à présenter des fenêtres en verre incurvées du sol au plafond. Les aménagements intérieurs vastes, allant de un à cinq chambres à coucher et disposant d'un réseau de duplex et double hauteur des espaces, ont été conçus et finis par Thomas Juul-Hansen. Quatre étages de la tour sont consacrés à des équipements state-of-the-art: un immense centre de remise en forme, le Club eau magnifiquement aménagées, des équipements childrens uniques, et L'observatoire de la rue de l'Ouest, un 64ème étage spectaculaire espace de divertissement en plein air avec apparemment vues infinies de New York et au-delà. penthouse. ",
//                            realtor = "Estate_Luxury",
//                            entryDate = Utils.getLongFormatDate(),
//                            modificationDate = null
//                        ),
//                        address = Address(
//                            district = "Financial District",
//                            number = 123,
//                            street = "Broadway",
//                            city = "New York"
//                        ),
//                        lat = 40.708839,
//                        lng = -74.011133,
//                        listPhoto = listPhoto,
//                        interest = Interest(subway = true, store = true, school = true),
//                    )
//                )
//                dao.insertEstate(
//                    Estate(
//                        primaryEstateData = PrimaryEstateData(
//                            estateType = "Appartment",
//                            price = 4_108_300,
//                            surface = 163.0,
//                            rooms = 8,
//                            landSize = null,
//                            soldDate = Utils.getLongFormatDate(),
//                            onSale = false
//                        ),
//                        secondaryEstateData = SecondaryEstateData(
//                            bedroom = 4,
//                            bathroom = 3,
//                            description = "iKeys Realty est ravie de vous présenté une copropriété de luxe où les résidents profitent du parc le plus célèbre de la ville qui se développe en face d'eux. Avec des finitions de haute qualité et des vues fascinantes sur Central Park, ce penthouse de 4 chambres et 3 salles de bains est un portrait de la grandeur contemporaine de la ville.\n" +
//                                    "\n" +
//                                    "Les caractéristiques de cet appartement de 1757 pieds carrés comprennent de beaux parquets en chêne blanc, des fenêtres du sol au plafond avec des cadres en chêne blanc et des volets motorisés, une cheminée, un système de chauffage, une ventilation et une sèche-linge, un balcon et une terrasse spacieux et une terrasse privée sur le toit avec une vue imprenable sur le parc, avec un lavabo extérieur et un jacuzzi.\n" +
//                                    "\n" +
//                                    "La maison commence par un salon, une salle à manger et une cuisine à aire ouverte saturée de lumière naturelle. Le salon et la salle à manger donnent sur Central Park, tandis que la cuisine est équipée d'un îlot à manger, d'armoires laquées gris mat sur mesure, de comptoirs élégants, d'un dosseret en verre et d'une série de fenêtres.\n" +
//                                    "\n" +
//                                    "La chambre principale offre une vue sur le parc, une paire d'armoires encastrées et une salle de bain privée impeccable décorée de carreaux Bianco Alanur, de lampes Hansgrohe nickel satiné et d'une grande douche à l'italienne. La deuxième chambre a une salle de bain complète et un accès au balcon, tandis que les troisième et quatrième chambres ont des armoires privées et un accès facile à une troisième salle de bain complète.\n" +
//                                    "\n" +
//                                    "Cette propriété est un condominium à service complet situé directement à Central Park, dans le quartier emblématique de Harlem. Les installations comprennent un concierge à plein temps, une salle de jeux, une salle de jeux pour enfants, une salle de sport ultramoderne, un salon exclusif pour les résidents et une incroyable terrasse sur le toit avec une vue imprenable sur le parc et les toits de Manhattan. Le bâtiment se trouve à distance de marche de dizaines de restaurants, cafés, bars et boutiques populaires qui bordent les avenues Malcolm X et Adam Clayton Powell Jr. et à cinq minutes de Harlem Meer, North Woods et The Blockhouse. Des places de parking privées, des unités de stockage et des cabines sont disponibles. Les animaux sont les bienvenus. ",
//                            realtor = "IKeys Realty",
//                            entryDate = Utils.getLongFormatDate(),
//                            modificationDate = null,
//                            firebaseId = 874946516321
//                        ),
//                        address = Address(
//                            district = "Central Park",
//                            number = 1801,
//                            complement = " Appt 58",
//                            street = "Adam Clayton Powell Jr Blvd",
//                            city = "New York"
//                        ),
//                        lat = 40.799528,
//                        lng = -73.954833,
//                        listPhoto = listPhoto,
//                        interest = Interest(),
//
//                        )
//                )
//            }
        }
    }

}
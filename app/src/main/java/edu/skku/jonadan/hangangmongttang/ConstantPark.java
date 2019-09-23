package edu.skku.jonadan.hangangmongttang;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstantPark {

    public static final int GWANGNARU = 0;
    public static final int JAMSIL = 1000;
    public static final int TTUKSEOM = 2000;
    public static final int JAMWON = 3000;
    public static final int BANPO = 4000;
    public static final int ICHON = 5000;
    public static final int YEOUIDO = 6000;
    public static final int MANGWON = 7000;
    public static final int NANJI = 8000;
    public static final int GANGSEO = 9000;
    public static final int YANGHWA = 10000;

    public static final ArrayList<ParkingLot> GWANGNARU_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(GWANGNARU+1,
                            "광나루 1주차장", 37.554935, 127.123507, 49),
                    new ParkingLot(GWANGNARU+2,
                            "광나루 2주차장", 37.546766, 127.120526, 119),
                    new ParkingLot(GWANGNARU+3,
                            "광나루 3주차장", 37.541445, 127.116299, 191),
                    new ParkingLot(GWANGNARU+4,
                            "광나루 4주차장", 37.527584, 127.106207)
            )
    );

    public static final ArrayList<ParkingLot> JAMSIL_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(JAMSIL+1,
                            "잠실 1주차장", 37.520083, 127.094253, 135),
                    new ParkingLot(JAMSIL+2,
                            "잠실 2주차장", 37.517627, 127.087653, 14),
                    new ParkingLot(JAMSIL+3,
                            "잠실 3주차장", 37.517770, 127.081069, 44),
                    new ParkingLot(JAMSIL+4,
                            "잠실 4주차장", 37.517864, 127.078516, 278)
            )
    );

    public static final ArrayList<ParkingLot> TTUKSEOM_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(TTUKSEOM+1,
                            "뚝섬 1주차장", 37.527642, 127.078011, 67),
                    new ParkingLot(TTUKSEOM+2,
                            "뚝섬 2주차장", 37.528961, 127.073451, 356),
                    new ParkingLot(TTUKSEOM+3,
                            "뚝섬 3주차장", 37.530688, 127.067464, 99),
                    new ParkingLot(TTUKSEOM+4,
                            "뚝섬 4주차장", 37.531431, 127.064311, 136)
            )
    );

    public static final ArrayList<ParkingLot> JAMWON_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(JAMWON+1,
                            "잠원 1주차장", 37.528724, 127.020498, 98),
                    new ParkingLot(JAMWON+2,
                            "잠원 2주차장", 37.527184, 127.020004, 30),
                    new ParkingLot(JAMWON+3,
                            "잠원 3주차장", 37.526707, 127.018138, 305),
                    new ParkingLot(JAMWON+4,
                            "잠원 4주차장", 37.524938, 127.015895, 70),
                    new ParkingLot(JAMWON+5,
                            "잠원 5주차장", 37.520210, 127.010979, 17),
                    new ParkingLot(JAMWON+6,
                            "잠원 6주차장", 37.518434, 127.009046, 215)
            )
    );

    public static final ArrayList<ParkingLot> BANPO_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(BANPO+1,
                            "반포 1주차장", 37.513024, 127.002556, 332),
                    new ParkingLot(BANPO+2,
                            "반포 2주차장", 37.509941, 126.995916, 257),
                    new ParkingLot(BANPO+3,
                            "반포 3주차장", 37.506630, 126.985103, 46)
            )
    );

    public static final ArrayList<ParkingLot> ICHON_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(ICHON+1,
                            "이촌 1주차장", 37.517877, 126.992042, 35),
                    new ParkingLot(ICHON+2,
                            "이촌 2주차장", 37.515815, 126.982589, 95),
                    new ParkingLot(ICHON+3,
                            "이촌 3주차장", 37.517429, 126.970465, 172),
                    new ParkingLot(ICHON+4,
                            "이촌 4주차장", 37.521562, 126.959626, 33)
            )
    );

    public static final ArrayList<ParkingLot> YEOUIDO_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(YEOUIDO+1,
                            "여의도 1주차장", 37.523008, 126.939322, 462),
                    new ParkingLot(YEOUIDO+2,
                            "여의도 2주차장", 37.528853, 126.931172, 171),
                    new ParkingLot(YEOUIDO+3,
                            "여의도 3주차장", 37.532363, 126.923498, 800),
                    new ParkingLot(YEOUIDO+4,
                            "여의도 4주차장", 37.526262, 126.912893, 141),
                    new ParkingLot(YEOUIDO+5,
                            "여의도 5주차장", 37.517217, 126.935695, 217)
            )
    );

    public static final ArrayList<ParkingLot> MANGWON_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(MANGWON+1,
                            "망원 1주차장", 37.544488, 126.911168, 149),
                    new ParkingLot(MANGWON+2,
                            "망원 2주차장", 37.549894, 126.902333, 97),
                    new ParkingLot(MANGWON+3,
                            "망원 3주차장", 37.554443, 126.902145, 138),
                    new ParkingLot(MANGWON+4,
                            "망원 4주차장", 37.556384, 126.893929, 194)
            )
    );

    public static final ArrayList<ParkingLot> NANJI_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(NANJI+1,
                            "난지 1주차장", 37.570183, 126.872927, 241),
                    new ParkingLot(NANJI+2,
                            "난지 2주차장", 37.570037, 126.872745, 110),
                    new ParkingLot(NANJI+3,
                            "난지 3주차장", 37.569000, 126.875059, 193)
            )
    );

    public static final ArrayList<ParkingLot> GANGSEO_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(GANGSEO+1,
                            "강서 1주차장", 37.585177, 126.819326, 67)
            )
    );

    public static final ArrayList<ParkingLot> YANGHWA_PARKING_LOTS = new ArrayList<>(
            Arrays.asList(
                    new ParkingLot(YANGHWA+1,
                            "양화 1주차장", 37.537489, 126.903317, 88),
                    new ParkingLot(YANGHWA+2,
                            "양화 2주차장", 37.539885, 126.900467, 20),
                    new ParkingLot(YANGHWA+3,
                            "양화 3주차장", 37.544734, 126.892404, 223),
                    new ParkingLot(YANGHWA+4,
                            "양화 4주차장", 37.545154, 126.891631, 100),
                    new ParkingLot(YANGHWA+5,
                            "양화 5주차장", 37.545790, 126.891058, 70)
            )
    );

    public static final ArrayList<Park> PARK_LIST = new ArrayList<Park>(
            Arrays.asList(
                    new Park(GWANGNARU, "광나루 한강공원", 37.548844, 127.120029, GWANGNARU_PARKING_LOTS),
                    new Park(JAMSIL, "잠실 한강공원", 37.517993, 127.081944, JAMSIL_PARKING_LOTS),
                    new Park(TTUKSEOM, "뚝섬 한강공원", 37.529422, 127.073980, TTUKSEOM_PARKING_LOTS),
                    new Park(JAMWON, "잠원 한강공원", 37.520729, 127.012251, JAMWON_PARKING_LOTS),
                    new Park(BANPO, "반포 한강공원", 37.509815, 126.994755, BANPO_PARKING_LOTS),
                    new Park(ICHON, "이촌 한강공원", 37.516026, 126.975832, ICHON_PARKING_LOTS),
                    new Park(YEOUIDO, "여의도 한강공원", 37.526461, 126.933682, YEOUIDO_PARKING_LOTS),
                    new Park(MANGWON, "망원 한강공원", 37.555045, 126.895960, MANGWON_PARKING_LOTS),
                    new Park(NANJI, "난지 한강공원", 37.566202, 126.876319, NANJI_PARKING_LOTS),
                    new Park(GANGSEO, "강서 한강공원", 37.588136, 126.815235, GANGSEO_PARKING_LOTS),
                    new Park(YANGHWA, "양화 한강공원", 37.538334, 126.902265, YANGHWA_PARKING_LOTS)
            )
    );
}

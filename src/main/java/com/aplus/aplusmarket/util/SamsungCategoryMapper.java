package com.aplus.aplusmarket.util;

import java.util.HashMap;
import java.util.Map;

public class SamsungCategoryMapper {

    // URL -> categoryId 매핑
    public static final Map<String, Long> CATEGORY_MAP = new HashMap<>();
    public static final Map<String, Long> CATEGORY_CURRENT_MAP = new HashMap<>();

    // categoryId -> categoryName 매핑
    public static final Map<Long, String> CATEGORY_NAMES = new HashMap<>();
    public static final Map<Long, Long> CATEGORY_PARENT_MAP = new HashMap<>();




    static {
        // ✅ 모바일 카테고리
        CATEGORY_MAP.put("https://www.samsung.com/sec/smartphone/all-smartphone/", 2L); // 스마트폰
        CATEGORY_MAP.put("https://www.samsung.com/sec/galaxybook/all-galaxybook/", 3L); // 노트북
        CATEGORY_MAP.put("https://www.samsung.com/sec/tablets/all-tablets/", 4L); // 태블릿
        CATEGORY_MAP.put("https://www.samsung.com/sec/watches/all-watches/", 5L); // 웨어러블
        CATEGORY_MAP.put("https://www.samsung.com/sec/buds/all-buds/", 5L); // 갤럭시 버즈 (웨어러블)
        CATEGORY_MAP.put("https://www.samsung.com/sec/rings/all-rings/", 5L); // 갤럭시 링 (웨어러블)

        //  PC/주변기기 카테고리
        CATEGORY_MAP.put("https://www.samsung.com/sec/desktop/all-desktop/", 7L); // 데스크탑
        CATEGORY_MAP.put("https://www.samsung.com/sec/monitors/all-monitors/", 8L); // 모니터
        CATEGORY_MAP.put("https://www.samsung.com/sec/memory-storage/all-memory-storage/", 9L); // 메모리/스토리지
        CATEGORY_MAP.put("https://www.samsung.com/sec/printers/all-printers/", 10L); // 프린터
        CATEGORY_MAP.put("https://www.samsung.com/sec/printer-supplies/all-printer-supplies/", 11L); // 토너 잉크

        // TV & 오디오 카테고리
        CATEGORY_MAP.put("https://www.samsung.com/sec/tvs/all-tvs/", 13L); // TV
        CATEGORY_MAP.put("https://www.samsung.com/sec/moving_style/all-moving_style/", 14L); // 무빙스타일
        CATEGORY_MAP.put("https://www.samsung.com/sec/samsung-audio/all-samsung-audio/", 15L); // 오디오
        CATEGORY_MAP.put("https://www.samsung.com/sec/harman-life-style-audio/all-harman-life-style-audio/", 15L); // 오디오

        // 주방가전 카테고리
        CATEGORY_MAP.put("https://www.samsung.com/sec/refrigerators/all-refrigerators/", 16L); // 냉장고
        CATEGORY_MAP.put("https://www.samsung.com/sec/kimchi-refrigerators/all-kimchi-refrigerators/", 17L); // 김치냉장고
        CATEGORY_MAP.put("https://www.samsung.com/sec/cooking-appliances/all-cooking-appliances/", 18L); // 조리기기
        CATEGORY_MAP.put("https://www.samsung.com/sec/dishwashers/all-dishwashers/", 19L); // 식기세척기
        CATEGORY_MAP.put("https://www.samsung.com/sec/water-purifier/all-water-purifier/", 20L); // 정수기
        CATEGORY_MAP.put("https://www.samsung.com/sec/kitchen-small-appliance/all-kitchen-small-appliance/", 21L); // 소형가전

        // 리빙가전 카테고리
        CATEGORY_MAP.put("https://www.samsung.com/sec/washers-and-dryers/all-washers-and-dryers/", 23L); // 세탁기 & 건조기
        CATEGORY_MAP.put("https://www.samsung.com/sec/airdressers-and-shoedressers/all-airdressers-and-shoedressers/", 24L); // 에어드레서 & 슈드레서
        CATEGORY_MAP.put("https://www.samsung.com/sec/air-conditioners/all-air-conditioners/", 25L); // 에어컨
        CATEGORY_MAP.put("https://www.samsung.com/sec/system-air-conditioners/all-system-air-conditioners/", 25L); // 시스템 에어컨 (에어컨과 같은 카테고리)
        CATEGORY_MAP.put("https://www.samsung.com/sec/air-cleaner/all-air-cleaner/", 26L); // 공기청정기
        CATEGORY_MAP.put("https://www.samsung.com/sec/dehumidifier/all-dehumidifier/", 27L); // 제습기
        CATEGORY_MAP.put("https://www.samsung.com/sec/vacuum-cleaners/all-vacuum-cleaners/", 28L); // 청소기
        CATEGORY_MAP.put("https://www.samsung.com/sec/small-appliances/all-small-appliances/", 29L); // 소형가전
        CATEGORY_MAP.put("https://www.samsung.com/sec/living-accessories/all-living-accessories/", 29L); // 리빙 액세서리 (소형가전과 동일한 카테고리)




        // 모바일 카테고리
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=33010000&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 2L); // 스마트폰
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=39120000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 3L); // 노트북
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=33020000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 4L); // 태블릿
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=33110000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 5L); // 웨어러블
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=33120000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 5L); // 갤럭시 버즈 (웨어러블)
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100040645&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 5L); // 갤럭시 링 (웨어러블)

        // PC/주변기기 카테고리
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=39020000&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 7L); // 데스크탑
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=39030000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 8L); // 모니터
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=40030000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 9L); // 메모리/스토리지
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=39040000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 10L); // 프린터
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=39070000&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 11L); // 토너 잉크

        // TV & 오디오 카테고리
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=34060000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 13L); // TV
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100043573&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 14L); // 무빙스타일
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=41040000&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 15L); // 오디오

        // 주방가전 카테고리
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=36010000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 16L); // 냉장고
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=36020000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 17L); // 김치냉장고
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=36030000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 18L); // 조리기기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=36080000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 19L); // 식기세척기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100020195&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 20L); // 정수기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100037843&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 21L); // 소형가전

        // 리빙가전 카테고리
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100043932&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 23L); // 세탁기 & 건조기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100043952&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 24L); // 에어드레서 & 슈드레서
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=37010000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 25L); // 에어컨
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100024278&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 25L); // 시스템 에어컨 (에어컨과 같은 카테고리)
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=37040000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 26L); // 공기청정기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=100043590&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=N&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 27L); // 제습기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=37030000&sortType=10&page=1&rows=29&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 28L); // 청소기
        CATEGORY_CURRENT_MAP.put("https://www.samsung.com/sec/cxhr/pf/goodsList?searchFilter=&dispClsfNo=37050000&sortType=10&page=1&rows=30&ehcacheYn=Y&soldOutExceptYn=Y&pfFasterUseYn=Y&secApp=false&secIos=false&aiscCtgYn=N", 29L); // 소형가전

        // CATEGORY_ID -> CATEGORY_NAME 매핑
        CATEGORY_NAMES.put(1L, "모바일");
        CATEGORY_NAMES.put(2L, "스마트폰");
        CATEGORY_NAMES.put(3L, "노트북");
        CATEGORY_NAMES.put(4L, "태블릿");
        CATEGORY_NAMES.put(5L, "웨어러블");

        CATEGORY_NAMES.put(6L, "PC/주변기기");
        CATEGORY_NAMES.put(7L, "데스크탑");
        CATEGORY_NAMES.put(8L, "모니터");
        CATEGORY_NAMES.put(9L, "메모리/스토리지");
        CATEGORY_NAMES.put(10L, "프린터");
        CATEGORY_NAMES.put(11L, "토너 잉크");

        CATEGORY_NAMES.put(12L, "TV & 오디오");
        CATEGORY_NAMES.put(13L, "TV");
        CATEGORY_NAMES.put(14L, "무빙스타일");
        CATEGORY_NAMES.put(15L, "오디오");

        CATEGORY_NAMES.put(16L, "주방가전");
        CATEGORY_NAMES.put(17L, "김치냉장고");
        CATEGORY_NAMES.put(18L, "조리기기");
        CATEGORY_NAMES.put(19L, "식기세척기");
        CATEGORY_NAMES.put(20L, "정수기");
        CATEGORY_NAMES.put(21L, "소형가전");

        CATEGORY_NAMES.put(22L, "리빙가전");
        CATEGORY_NAMES.put(23L, "세탁기 & 건조기");
        CATEGORY_NAMES.put(24L, "에어드레서 & 슈드레서");
        CATEGORY_NAMES.put(25L, "에어컨");
        CATEGORY_NAMES.put(26L, "공기청정기");
        CATEGORY_NAMES.put(27L, "제습기");
        CATEGORY_NAMES.put(28L, "청소기");
        CATEGORY_NAMES.put(29L, "소형가전");


        //모바일
        CATEGORY_PARENT_MAP.put(2L, 1L);
        CATEGORY_PARENT_MAP.put(3L, 1L);
        CATEGORY_PARENT_MAP.put(4L, 1L);
        CATEGORY_PARENT_MAP.put(5L, 1L);

        //주변기기
        CATEGORY_PARENT_MAP.put(7L, 6L);
        CATEGORY_PARENT_MAP.put(8L, 6L);
        CATEGORY_PARENT_MAP.put(9L, 6L);
        CATEGORY_PARENT_MAP.put(10L, 6L);
        CATEGORY_PARENT_MAP.put(11L, 6L);

        //티비오디오
        CATEGORY_PARENT_MAP.put(13L, 12L);
        CATEGORY_PARENT_MAP.put(14L, 12L);
        CATEGORY_PARENT_MAP.put(15L, 12L);
        //주방가전
        CATEGORY_PARENT_MAP.put(17L, 16L);
        CATEGORY_PARENT_MAP.put(18L, 16L);
        CATEGORY_PARENT_MAP.put(19L, 16L);
        CATEGORY_PARENT_MAP.put(20L, 16L);
        CATEGORY_PARENT_MAP.put(21L, 16L);
        //리빙가전
        CATEGORY_PARENT_MAP.put(23L, 22L);
        CATEGORY_PARENT_MAP.put(24L, 22L);
        CATEGORY_PARENT_MAP.put(25L, 22L);
        CATEGORY_PARENT_MAP.put(26L, 22L);
        CATEGORY_PARENT_MAP.put(27L, 22L);
        CATEGORY_PARENT_MAP.put(28L, 22L);
        CATEGORY_PARENT_MAP.put(29L, 22L);
    }
}

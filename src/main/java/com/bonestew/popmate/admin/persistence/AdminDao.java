package com.bonestew.popmate.admin.persistence;

import com.bonestew.popmate.admin.domain.MainBanner;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminDao {
    void insertMainBanner(@Param("popupStoreId") Long popupStoreId,
                          @Param("bannerImgUrl") String bannerImgUrl);

    MainBanner getOneMainBanner();

    void deleteBanner(Long bannerId);

    List<MainBanner> getMainBanner();
}

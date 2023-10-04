package com.bonestew.popmate.admin.application;

import com.bonestew.popmate.admin.domain.BackOfficePopupStore;
import com.bonestew.popmate.admin.domain.MainBanner;
import com.bonestew.popmate.admin.persistence.AdminDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminDao adminDao;

    public List<BackOfficePopupStore> getPopupStore() {
        return adminDao.getPopupStore();
    }

    public void insertMainBanner(Long popupStoreId, String bannerImgUrl) {
        adminDao.insertMainBanner(popupStoreId, bannerImgUrl);
    }

    public MainBanner getOneMainBanner() {
        return adminDao.getOneMainBanner();
    }

    public void deleteBanner(Long bannerId) {
        adminDao.deleteBanner(bannerId);
    }

    public List<MainBanner> getMainBanner() {
        return adminDao.getMainBanner();
    }

}

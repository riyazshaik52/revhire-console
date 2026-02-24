package com.revhire.service;

import com.revhire.dao.ResumeDAO;
import com.revhire.model.Resume;

public class ResumeService {

    private final ResumeDAO resumeDAO = new ResumeDAO();

    public boolean createResume(Long userId,
                                String objective,
                                String education,
                                String experience,
                                String skills,
                                String projects) {

        Resume existing = resumeDAO.getResumeByUserId(userId);

        if (existing != null) {
            System.out.println("Resume already exists. Use update instead.");
            return false;
        }

        Resume resume = new Resume(userId, objective, education,
                experience, skills, projects);

        return resumeDAO.createResume(resume);
    }

    public Resume viewResume(Long userId) {
        return resumeDAO.getResumeByUserId(userId);
    }

    public boolean updateResume(Long userId,
                                String objective,
                                String education,
                                String experience,
                                String skills,
                                String projects) {

        Resume resume = new Resume(userId, objective,
                education, experience,
                skills, projects);

        return resumeDAO.updateResume(resume);
    }

    public boolean deleteResume(Long userId) {
        return resumeDAO.deleteResume(userId);
    }

    public Resume getResumeByUser(Long userId) {
        return resumeDAO.getByUserId(userId);
    }

}

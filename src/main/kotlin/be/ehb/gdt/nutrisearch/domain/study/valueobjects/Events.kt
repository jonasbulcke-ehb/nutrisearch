package be.ehb.gdt.nutrisearch.domain.study.valueobjects

data class StudyJoinedEvent(val studyId: String, val userinfoId: String)
data class StudyLeftEvent(val studyId: String, val userinfoId: String)
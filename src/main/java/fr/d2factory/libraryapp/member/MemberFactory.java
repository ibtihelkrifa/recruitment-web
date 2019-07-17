package fr.d2factory.libraryapp.member;

public class MemberFactory {


    private static int memberId = 0 ;





    public static Member createMember(MemberType memberType)
    {
        memberId++ ;
        switch (memberType){
            case STUDENT: return new Student(memberId,false);
            case STUDENT_FIRST_YEAR: return new Student(memberId,true);
            case RESIDENT: return new Resident(memberId);

        }
        return null;
    }



}

package Model.Entities.FamilyMember;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by adima on 10/03/2018.
 */

public class FamilyMemberListViewModel extends ViewModel {
    private LiveData<List<FamilyMember>> familyMembers;

    public void init(String serialNumber){
      //  familyMembers = CommentRepository.instance.getAllFamilyMembers;
    }

    public FamilyMemberListViewModel() {}

    public LiveData<List<FamilyMember>> getFamilyMembersList() {
        return familyMembers;
    }
}

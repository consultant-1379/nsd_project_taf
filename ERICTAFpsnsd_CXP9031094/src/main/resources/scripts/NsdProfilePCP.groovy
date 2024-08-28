import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.pcp.service.IProfileMgtService;
import com.ericsson.oss.pcp.service.domain.Profile;
import com.ericsson.oss.pcp.service.domain.ProfileAttribute;
import com.ericsson.oss.pcp.service.domain.ProfileProperties;
import com.ericsson.oss.cp.core.OCP;

import java.util.Collections;

import com.ericsson.oss.pcp.service.domain.ImportProfileResult;

class NsdProfilePCP {

	private IProfileMgtService service;
	private ImportProfileResult importProFile;
	final String PASSED = "OK"

	/*
	 * Creating a PCP profile
	 */
	public def createProfile(String name, String desc) {
		try          {
			final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();
			final ProfileProperties profileProperties = new ProfileProperties.ProfilePropertiesBuilder(null, name, OCP.getUserId()).setDescription(desc).setModifiedBy(OCP.getUserId()).build();
			final Profile profile = new Profile(profileProperties, profileAttributes);
			getprofileMgtUICoreService().createProfile(profile, OCP.getUserId());
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return PASSED;
	}
	/*
	 * Finding Profile based on name of Profile Created earlier
	 */
	public def findProfileByName(String name) {
		try          {
			final Profile profile = getprofileMgtUICoreService().findProfileByName(name, OCP.getUserId());
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return PASSED;
	}
	/*
	 * Deleting a created Profile
	 */
	public def deleteProfileByName(String newProfileName) {
		try          {
			final Profile profile = getprofileMgtUICoreService().deleteProfileByName(newProfileName, OCP.getUserId());
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return PASSED;
	}

	
	/*
	 * Export Profile a created Profile
	 */
	public def exportProfileByName(String newProfileName,String exportFile) {
		try          {
			
			getprofileMgtUICoreService().exportProfile("Testing_PCP","/home/nmsadm/sid.profile",OCP.getUserId());//(profileName, exportFile, userId);
			
			/*Profile profile = profileMgtService.findProfileByName(profileName, userId);
			if (profile == null) {
				throw new ProfileMgtException(String.format("No Profile found by the name %s.", profileName));
			}*/
			
			/*profileMgtService.exportProfile(profileName, exportFile, userId);
			Profile profile = profileMgtService.findProfileByName(profileName, userId);
			if (profile == null) {
				throw new ProfileMgtException(String.format("No Profile found by the name %s.", profileName));
			}
			*/
				}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return PASSED;
	}

	
	/*
	 * Export Profile a created Profile
	 */
	public def importProfileByName(String newProfileName,String overwrite) {//profileMgtService.importProfile(importfile, overwrite, userId);
		boolean boolean2 = Boolean.parseBoolean(overwrite);
		try          {
			importProFile = getprofileMgtUICoreService().importProfile("/home/nmsadm/sid.profile", boolean2,OCP.getUserId());//(profileName, exportFile, userId);
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return PASSED;
	}

	
	



	/*
	 * updating Profile Already Created
	 */
	public def updateProfile(String originalName) {

		String newProfileName = "pcpPro2";
		String newProfileDesc = "EditedProfile";
		//                                                            String[] profileData = {PASSED, newProfileName, newProfileDesc};
		try          {
			final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();

			final Profile profile = getprofileMgtUICoreService().findProfileByName(originalName, OCP.getUserId());
			ProfileProperties originalProfile = profile.getProfileProperties();

			final ProfileProperties profileProperties = new ProfileProperties.ProfilePropertiesBuilder(originalProfile
					.getId(), newProfileName, originalProfile.getOwnedBy()).setDescription(newProfileDesc).setCreatedTime(
					originalProfile.getCreatedTime()).setModifiedBy(OCP.getUserId()).build();

			final Profile profileForUpdate = new Profile(profileProperties, profileAttributes);

			//calling ProfileMgmnt Service to update Profile with new name and description

			getprofileMgtUICoreService().updateProfile(profileForUpdate, OCP.getUserId());

			//clean up

			deleteProfileByName(newProfileName);
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		return PASSED;
	}
	public def deleteProfileProperty(String originalName) {
		String profileDeleted="FAILED";
			//                                                           String[] profileData = {PASSED, newProfileName, newProfileDesc};
				try          {
					final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();
		
					final Profile profile = getprofileMgtUICoreService().findProfileByName(originalName, OCP.getUserId());
					ProfileProperties originalProfile = profile.getProfileProperties();
		
					/*final ProfileProperties profileProperties = new ProfileProperties.ProfilePropertiesBuilder(originalProfile
							.getId(), newProfileName, originalProfile.getOwnedBy()).setDescription(newProfileDesc).setCreatedTime(
							originalProfile.getCreatedTime()).setModifiedBy(OCP.getUserId()).build();*/
		
					final Profile profileForUpdate = new Profile(originalProfile, profileAttributes);
		
							
					getprofileMgtUICoreService().updateProfile(profileForUpdate, OCP.getUserId());
					final Profile updatedProfile = getprofileMgtUICoreService().findProfileByName(originalName, OCP.getUserId());
					
					if(updatedProfile!=null)
					{
						if(updatedProfile.getProfileAttributes().size()==0)
						{
							profileDeleted="PASSED";
							return profileDeleted;
						}
					}
					
		
					
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				return profileDeleted;
			}

	public def updateProfileProperty(String originalName, String mimModel, String moc,String attrName, String attrValue) {
		String profileUpdated="FAILED";
		
			//                                                           String[] profileData = {PASSED, newProfileName, newProfileDesc};
	try          {
					final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();
					ProfileAttribute list= new ProfileAttribute(mimModel, moc, attrName,attrValue);
					profileAttributes.add(list);
		
					final Profile profile = getprofileMgtUICoreService().findProfileByName(originalName, OCP.getUserId());
					ProfileProperties originalProfile = profile.getProfileProperties();
		
					
					final Profile profileForUpdate = new Profile(originalProfile, profileAttributes);
		
							
					getprofileMgtUICoreService().updateProfile(profileForUpdate, OCP.getUserId());
					final Profile updatedProfile = getprofileMgtUICoreService().findProfileByName(originalName, OCP.getUserId());
					
					if(updatedProfile!=null)
					{
						
						Collection<ProfileAttribute> updatedAttributes=updatedProfile.getProfileAttributes();
						
						ProfileAttribute temp = null;
						if(updatedAttributes!=null)
						{
							
							Iterator  itr = updatedAttributes.iterator();
							while(itr.hasNext()){
			                      
							
							ProfileAttribute profAttr = (ProfileAttribute)itr.next();
							if(profAttr != null && profAttr.getMimModel().equals(mimModel) 
								&& profAttr.getMoc().equals(moc) 
								&& profAttr.getAttrName().equals(attrName) 
								&& profAttr.getAttrValue().equals(attrValue))
							{
								
								profileUpdated="PASSED";
								return profileUpdated;
							}
						}
					}
		
					
				}
				}
				catch(Exception e){
					return e.getMessage().toString();
				}
				return profileUpdated;
			}



	
	/*
	 * Verify a PCP profile
	 */
	public def verifyProfile(String name, String desc) {
		try          {
			final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();
			final ProfileProperties profileProperties = new ProfileProperties.ProfilePropertiesBuilder(null, name, OCP.getUserId()).setDescription(desc).setModifiedBy(OCP.getUserId()).build();
			final Profile profile = new Profile(profileProperties, profileAttributes);
			getprofileMgtUICoreService().createProfile(profile, OCP.getUserId());
			
			profile = getprofileMgtUICoreService().findProfileByName(name, OCP.getUserId());
			if(profile!=null){
				return PASSED;
			}
			
			
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
		
	}


	//=======================================================================================================================//
	/*
	 * Services
	 */

	public def getprofileMgtUICoreService(){
		final String profileMgtUICoreUrl = "rmi://masterservice:50042/ProfileManagementService";
		service= (IProfileMgtService) getRmiService(profileMgtUICoreUrl, IProfileMgtService.class);
		return service;
	}


	private static Object getRmiService(final String url, final Class clazz) {
		final RmiProxyFactoryBean plannedManagementRmiFactory = new RmiProxyFactoryBean();
		plannedManagementRmiFactory.setServiceInterface(clazz);
		plannedManagementRmiFactory.setServiceUrl(url);
		plannedManagementRmiFactory.setRefreshStubOnConnectFailure(true);
		plannedManagementRmiFactory.afterPropertiesSet();

		return ((FactoryBean) plannedManagementRmiFactory).getObject();
	}

}

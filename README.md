

ISTD 50.001 INTRODUCTION TO INFORMATION SYSTEMS AND PROGRAMMING

**1D Information Systems Design Project**
# **Amigo Project**


1004581	Toh Kai Feng

1004288	Bryan Phengan Hengardi 

1004628	Seah Qi Yan 

1004116 	Goh Shao Cong Shawn

1004241 	Amrish Dev Sandhu





# **Problem Statement**

Student Initiated projects are becoming an increasingly popular and validated way for students to apply their skills in a practical manner as emphasised by our very own ISTD pillar head, Prof Tony.  \
 \
However, starting off such projects or finding a suitable project to join may be difficult due to a lack of connections and experience. We also know that most project teams are currently built up through referrals and word of mouth, and a lack of a platform to either find people to do projects with aggravates the problems for students. 

We asked ourselves the following questions: 
1. **Connect** - How might we connect interested and relevantly skilled students for self-initiated projects?

2. **Empower** - How might we empower student-led project leads with project management skills?

3. **Enable** - How might we enable students to embark on new learning experiences together?

Old User Journey:

![](https://i.imgur.com/iVkilVW.png)



Students find it hard to initiate a project to collaborate on with people outside of their social circle.


# **Solution - Our Application**

Introducing Amigo Project, a project management social platform that brings together the features of Carousell and LinkedIn to help students connect with other like-minded self initiated individuals to learn together and embark on student-led projects together.

In detail Amigo Project can first be broken down into the following sections:



1. Onboarding (for first time users) and Sign Up Page
2. Sign Up/Sign In
3. Profile Setup
4. Discover Projects Section/ Home Page
5. Projects Application/Management
6. Chats 
7. Profile Viewing/Management

The diagram below illustrates how we envision the average user would use the app to connect with other individuals to participate or kickstart student-initiated projects.

![](https://i.imgur.com/BJuu8T7.png)


Diagram  1: Onboarding Screen Grabs invite and introduces new users to the app

## **1 | Onboarding**

Upon the first opening of the app after download, the user will be greeted with this onboarding section of the app. This section will only be shown once upon new users opening the app.

This section is meant to introduce and invite the user to continue using the app. It briefly explains the different functions of the app and at the end invites the user to sign up. We used shorter descriptions and simple one word adjectives to make our solution more attractive.

Like many other apps, users can choose to skip past this by clicking the signup button located at the bottom left of the screen.

Pre-existing users that have previously used the app on a different phone can choose to move to the sign in page and thereafter continue using the app.


![](https://i.imgur.com/HrOrNIi.png)



Fig 1: Onboarding Screen Grabs invite and introduces new users to the app

*This section was done to enhance the “completeness” and aesthetics of the entire app and does not contain any main features of the app pertaining to the problem statement




## **2 | Sign Up/Sign In**
For users that are not opening the app for the first time (this state saved within an attribute called isintroed in the phone’s system preferences), instead of the onboarding section, they will be greeted with the sign in page.

In this page/section, users are able to do exactly what it sounds like: Sign In/Sign up!

Our app uses the built in Firebase authentication using a legitimate email and password to register/manage users on the app. In the Sign Up page, users will be prompted to fill in 4 basic fields (First Name, Last Name, Email and Password) required for creating their account (further details will be gathered in the next “Profile Setup section after their email has been verified).

We have included unitary testing of the email and password(email must be in email format., password must be 6-15 characters with at least 1 capital letter and 1 number) using regex string formatting checks to accept only what the field needs. When the user clicks the signup button, the app will check if the fields are acceptable. Thereafter, if the fields are acceptable, an account will be created in firebase and a verification email (using firebase prebuilt verification email api) will be sent to the user. (Users will not be able to sign in if they have not verified their emails, as such they would be required to sign up with a valid existing email.)

Thereafter, users are then able to use their account’s email and password to sign in to the app. If the account exists in the Firebase authentication and the user has verified their email before, the user will be able to access the app.

![](https://i.imgur.com/RM0ABz4.png)


Fig 2: Screen Grabs From left, Sign Up, Sign In, Email Verification


## **3 | Profile Setup**

The profile setup section is the first section new users who first use the application would see. In this section, they are brought through the registration process step by step, to fill in key details such as their name, uploading a profile picture, what they’re looking for, and the skills they possess. 

We were impressed with Tinder’s profile setup process and modelled our process after theirs. 

Through this process, we will set up and collect information about 4 main attributes of the user that will be able to help guide the user’s experience in the app as well as allow for customized viewing of projects that the user will likely be interested in.

The setup process will be as follows (Shown in screengrab below, left to right):



1. Writing a bio/short excerpt/about me section that will be displayed to others when they view the user’s profile, aiding in communicating the user’s intent on the app as well as their interests, allowing people to connect with like minded individuals
2. Apart from the about me section in the user profile, there is another section called the “looking for” section which details what the user is currently looking to join (e.g. looking to join projects/collaborative learning/start up etc...)
3. Users will thereafter be able to select from a list of skill chips that they identify with. This would help to create a more customized experience for the user when viewing project listings later on. The selected skill chips would along with the looking fort section would help to filter from the numerous project listings on the app
4. Lastly, the user will have to add a profile photo to show others how they look like for easier identification and connections.


![](https://i.imgur.com/9xqyJNG.jpg)


Fig 3: Profile Setup Screen Grabs


## **4 | Home**

At the home page, they would be able to discover new projects. There would be a few categories displayed for them to choose from, namely _Learn, Hardware, Software and Start-Up_ for this project submission (which can be added on in future developments), and from each category they can view the various projects listed under those categories. There is also an option to _See all projects_ which brings the user to the projects viewing page listing all projects available. 

 \
Additionally, within the homepage itself, there is a preview of projects under_ Suggested For You _which display projects that have listed skills that the user has indicated in their profile. This preview aims to help optimise users’ project exploration experience by recommending projects that they would likely be interested in. There is also a search bar implemented on the homepage, whereby clicking on it would lead to a_ Search Fragment_ allowing the user to type in keywords and/or select skill chips relating to the project they wish to find. Upon clicking the _Search_ button, these inputs are sent through an intent to the _Project Listings_ viewing page, where a filter is applied on the RecyclerView (which is possible through the use of the Filterable interface on the projects adapter, _MyAdapter_).

We can also see a custom Chip navigation bar implemented through the use of the library _Ismael Divita Chip Navigation Bar ([https://github.com/ismaeldivita/chip-navigation-bar](https://github.com/ismaeldivita/chip-navigation-bar))_, which improved the aesthetics of the user interface. Clicking on the respective chips/icons in the navigation bar would bring the user to the 4 main pages of the application - Explore (Home page), Chats, My Projects, and Profile. This was implemented through the use of fragments with the navigation bar, where clicking on the navigation bar would change the view of the app (i.e. a different fragment is shown) through using a Fragment Container, allowing the user to cycle through the views. 


![](https://i.imgur.com/CmOUKHv.png)


Fig 4: Home-page and search function


## **5 | Chats**

The chat function was implemented so that those who are keen to connect with others and also find out more about their projects can utilise this function within the app itself.** **This would allow for human interaction and query response from user to user.

This makes it more friendly to users who would like to inquire more and also confirm details that may not be included within the project description with the project lead. It also offers a social aspect, for them to connect with and socialise with other like-minded individuals. 

The Chat tab was implemented as a nested fragment with 2 tabs using a tablayout manager. These 2 tabs are respectively CHATS, meaning active chats, and USERS, a list of all the users currently on the app. 

Users are able to speak to one another live using the app and all information of their chat is stored and retrieved from firebase. They are stored as a chatitem that has 3 information: The message, the sender and the receiver. With these 3 information, we are able to instantiate the chat between users through parsing the database. While this is a primitive way of creating a chat function, it allows us to expand on the data and information we can store and use in future expansion (ability to store date time, last read etc… to add more functionalities.)

![](https://i.imgur.com/LFSBFj0.png)



Fig 5: Chat Tab, All Users Display and an example of a chat between two users.



## **6 | Project Application/Management**

Upon finding a project that may interest them, the user may select the project to view an expanded form of the project. \
 \
The full project image, title and description can be seen as follows. \
 \
Furthermore, should they be interested to work and collaborate on the project, they may click on the apply button to apply for the project, following which they will need to await confirmation from the user who listed the project. 

The project lead who posted the project would be able to view the applicants who applied, and then be offered a choice to accept or to reject them.  \
 \
Upon acceptance, the one who applied for the project would be able to see the project move from the “Applied Projects” tab to the “Projects” tab, which serves as verification that his application was successful.


![](https://i.imgur.com/JDIb32s.jpg)



Fig 6: From left: Create Project listing, Applicant Viewing Activity, Project Management Activity, 

my activeprojects and my applied projects tab.



## **7 | Profile Viewing/Management**

Users may then view their own profile and update it from time to time should they acquire and learn new skills, or change what they are looking for and their bio (very much like LinkedIn).

This can be done via the profile page, where by clicking on the edit function they will be brought through the fields that they can edit to their liking.

The goal of allowing users to edit their profile as and when they want is to allow the accounts of the users to accurately present their progress, current skills and what they are looking for for the app to better meet their needs.


![](https://i.imgur.com/YCwww7o.jpg)


Fig 7: Profile View and Fields under Edit Profile





# **System Architecture**


## **System Overview**


![](https://i.imgur.com/FYdlG7v.png)



Diagram 2. System Architecture

As seen in the diagram above, our system architecture only relies on a single database without other nodes for the storage and retrieval of data. Users interaction with our application directly liaises with our database hosted by Firebase and requests for and generates the data they need. These data are then either retrieved or written to firebase as needed using the onDataChanged method implemented by the Firebase dependency. Below would be a detailed overview of our database structure and how we query and store data when users use our application.

*Note: Only data not stored on the database is one instance of data stored using SharePreferences on the client(user) side. This data denotes if the user has been through the onboarding introduction slides and if so, skips it. Hence users will only seethe onboarding slides once every download of the app(unless they delete their local cache)


## 


## **Database Structure**

We used Google’s Firebase to act as our database, using 2 main functions: Firebase Authentication for account management and Firebase Real-time Database for data storage and retrieval.The database is set up with 4 main child nodes attached to the root node. These correspond to respective custom classes in our project. 

![](https://i.imgur.com/qhnmPNi.png)


Fig. 8 Database Overview


### **Chats**

In the chats child node, the subnode would be the individual chat message id of every single chat message. This is generated using the Firebase method call of push which generates a unique ID every time the method is called.

In each child message node, there will be 3 attributes: 1. The message, 2. The sender and 3. The receiver. When a user accesses the chats tab of the app, the app is able to generate and retrieve their chat information from this node. The figure below shows an example of a child node in the chat node.



![](https://i.imgur.com/n01f21M.png)



Fig. 9 Chat child message node


### 


### **Projects**

Similar to how the chat node instantiates a unique id for each chat message, the creation of a project uses the same method to generate a unique project ID. Under this mode contains a host of information that is retrieved every time the project in question is displayed anywhere in the app, be it in the suggested projects section or the personal applied projects tab. 

One thing to note is that the applicantsinProject, category, skills required and usersinProject are all stored as arrays as there can be multiple data entries for those fields.


![](https://i.imgur.com/QqzNnhE.png)



Fig. 10 Projects child message node

All these information is populated when a user creates a project on our project creation activity. 

A list of the current applicants are stored in the Project and is updated every time a new user applies for the project, storing their user id in this attribute as an element in an Arraylist. When the application of a user is approved by the user that created the project (indicated by the createdby attribute which saves the user id of the user that created the project), the approved user’s user id will be removed from the applicantsinProject arraylist attribute and moved into the usersinProject arraylist attribute. These attributes are used for the population of the recyclerViews located in the project management activity that the project creator can see.


### 


### **User child nodes**

The user data is split into 2 different nodes for differentiations and segregation between information and data shown to other users (users_display) and information that are only accessible to the user themselves (users_private).


#### **Users_display**

This corresponds to a custom class with the same title shown in the diagram below. It hosts information that is to be shown whenever another user accesses the users profile, hence named display to indicate display information. 

Each user has a node that is named using their user id that is generated when an account is created in the authentication section of Firebase.

![](https://i.imgur.com/fvYUzvH.png)


Fig. 11 users_display node


#### **Users_private**

Similar to the users_display node, however, it stores potentially sensitive information such as the user’s email. Future implementation that might enquire the user’s sensitive information such as user’s phone number or credit card information (should they be required) can also be stored here to segregate the private information.

![](https://i.imgur.com/y1ftvFK.png)


Fig. 11 users_private node


# **Application of concepts/Code Optimization**

While there were many concepts taught in class, some were deprecated(like AsyncTasks). That said, here  are some ways we utilized concepts taught in the course and also to reduce redundancies in our application.


### **Observer Design Pattern**

The chat tab uses a similar approach to populate each user's chat to that of the observer design pattern. It uses the addValueEventListener to check for any change in the data of the database. This essentially functions the same way an observer would work with the database being the subject. From our understanding of this method, this method is made by the Firebase team such that it is always called last in wherever it is applied, essentially making sure that all changes have been made to the database before it is called, essentially making sure the database is the latest it can be. The code chunk below shows how the update of the recyclerview for the messages activity works.


```
fuser = FirebaseAuth.getInstance().getCurrentUser();
mref = FirebaseDatabase.getInstance().getReference("users_display");
mref.addValueEventListener(new ValueEventListener() {
   @Override
   public void onDataChange(@NonNull DataSnapshot snapshot) {
       for (DataSnapshot ds : snapshot.getChildren()) {
           if(ds.getKey().equals(getID)) {
               users_display user = ds.getValue(users_display.class);
               personyoutalkingto.setText(user.getName());
               if (user.getProfile_picture().equals("")) {
                   profilepic.setImageResource(R.mipmap.ic_launcher_round);
               } else {
                   Picasso.get().load(user.getProfile_picture()).into(profilepic);
                   imgurlleft = user.getProfile_picture();}
               if (user.getStatus().equals("online")) {
                   icon_on.setVisibility(View.VISIBLE);
                   icon_off.setVisibility(View.GONE);
               } else {
                   icon_off.setVisibility(View.VISIBLE);
                   icon_on.setVisibility(View.GONE);}
           }
           else if(ds.getKey().equals(fuser.getUid())){
               imgurlright = ds.getValue(users_display.class).getProfile_picture();
           }
       }
       ReadMessages(fuser.getUid(), getID, imgurlleft, imgurlright);
   }
   @Override
   public void onCancelled(@NonNull DatabaseError error) {}
});
```


Future improvements in functionality like last read, last message, message notifications etc. will also utilise the observer design pattern. Group chats will also similarly use this design pattern.


### **Adapter Design Pattern**

We have 5 different adapters that 4 of which are similar adapters for recyclerviews (extends RecyclerView.Adapter&lt;messageAdapter.Viewholder>) around the app and 1 of which serves as an adapter for filtering of options (`extends RecyclerView.Adapter&lt;MyAdapter.myholder> implements Filterable) ` during use of the search function in the main home page.

Note: We chose to use recyclerview as it is more memory and computationally efficient than views with similar functions like listview.

An example using the adapter for the search function filtering.


```
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myholder> implements Filterable {
       private List<Project> projectsList;
       private List<Project> projectListAll;
       private StorageReference storageReference,projectref;
       public boolean isEmpty;

       public MyAdapter(List<Project> projectsList) {
           this.projectsList = projectsList;
           projectListAll = new ArrayList<>(projectsList);
       }
       public void setProjectsList(List<Project> projectsList) {
           this.projectsList = projectsList;
           notifyDataSetChanged();
       }
       public void setProjectListAll(List<Project> projectListAll) {
           this.projectListAll = projectListAll;
       }
       @NonNull
       @Override
       public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           View view = inflater.inflate(R.layout.row, parent, false);
           return new myholder(view);
       }
       @Override
       public void onBindViewHolder(@NonNull myholder holder, int position) {
           holder.mytext1.setText(projectsList.get(position).getProjectitle());
           holder.mytext2.setText(projectsList.get(position).getProjectdescription());
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(v.getContext(), ProjectDetails.class);
                   intent.putExtra("ProjectID", projectsList.get(position).getProjectID());
                   v.getContext().startActivity(intent);
               }
           });
           Picasso.get().load(projectsList.get(position).getThumbnail()).into(holder.thumbnail);
       }

       @Override
       public int getItemCount() {
           int length = projectsList.size();
           return length;
       }
       @Override
       public Filter getFilter() {
           return projectsFilter;
       }
       public boolean checkIsEmpty() {
           return isEmpty;}
       private Filter projectsFilter = new Filter() {
       @Override
       protected FilterResults performFiltering(CharSequence constraint) {
           List<Project> filteredProjects = new ArrayList<>();

           if (constraint == null || constraint.length() == 0) {
               filteredProjects.addAll(projectListAll);
           }
           else {
               String filterPattern = constraint.toString().toLowerCase().trim();

               for (Project project : projectListAll) {
                   if (project.getProjectitle().toLowerCase().contains(filterPattern)
                           || project.getProjectdescription().toLowerCase().contains(filterPattern)
                           || project.getCreatedby().toLowerCase().contains(filterPattern))
                   {filteredProjects.add(project);
                   }
               }
           }
           FilterResults results = new FilterResults();
           results.values = filteredProjects;
           if (filteredProjects.isEmpty()) {
               isEmpty = true;
           }
           return results;
       }

       @Override
       protected void publishResults(CharSequence constraint, FilterResults results) {
           projectsList.clear();
           projectsList.addAll((List) results.values);
           notifyDataSetChanged();}
   };

       public class myholder extends RecyclerView.ViewHolder {
           TextView mytext1, mytext2;
           ImageView thumbnail;
           public myholder(@NonNull View itemView) {
               super(itemView);
               thumbnail = itemView.findViewById(R.id.project_picture);
               mytext1 = itemView.findViewById(R.id.info_text);
               mytext2 = itemView.findViewById(R.id.description);
           }
       }
   }
```


The other 4 adapters are similar albeit have different functions as they serve different recyclerviews which require different data fields and different layout formats (different viewholders have to be used in each adapter.


### **FirebaseMethods class**

We created a custom class called FirebaseMethods to house all the methods and functions related to firebase. This was done as we noticed the frequency of these method calls and found that the best way to declutter the code and to reduce redundancy was to create a class to house such methods.

The custom class has a few attributes being:


```
final String TAG = "FirebaseMethods";

private FirebaseAuth mAuth;
private FirebaseAuth.AuthStateListener mAuthstatelistner;
private String userID;
private FirebaseDatabase mfirebasedatabase;
private DatabaseReference myRef;
private String[] cleanstring;
private Context mContext;
```


These attributes are all needed throughout the class due to the various needs to access the database or the Authentication side of Firebase and are thus instantiated in the constructor below whenever the FirebaseMethods class is instantiated for easy access and reference throughout the class. The string TAG is used for LOGD calls and debugging purposes. 

The class has only 1 constructor that takes in only 1 argument(current activity context - needed for several methods in the class) but as mentioned above, instantiates all the attributes above with information relevant to the current activity and user when called.

This is the constructor for the FirebaseMethods class:


```
public FirebaseMethods(Context context){
   mAuth = FirebaseAuth.getInstance();
   mContext = context;
   mfirebasedatabase = FirebaseDatabase.getInstance();
   myRef = mfirebasedatabase.getReference();


No current user when signing up.If this check not done, the app will crash
   if(mAuth.getCurrentUser()!=null){
       userID = mAuth.getCurrentUser().getUid();
   }
}
```




It holds the following firebase access methods used throughout the app.


```
updates the display name of the user by updating the name field in the database.
Used when editing the user profile.
public void updateName(String displayName){...}

updates the profile picture of the user by updating the name field in the database.
Used when editing the user profile.
public void updateProfilePicture(String pictureURL){...}

updates the profile bio of the user by updating the name field in the database.
Used when editing the user profile.
public void updateBio(String Bio){...}

updates the about me section of the user by updating the name field in the database. Used when editing the user profile.
public void updateAboutMe(String aboutMe){...}

updates the looking for section of the user by updating the name field in the database. Used when editing the user profile.
public void updateLookingFor(String lookingfor){...}

updates the skill chips of the user by updating the name field in the database. Used when editing the user profile.
public void updateSkillChips(String skillChipsString){...}

Checks if the email used to register the account exists already in the system.
Used in Signup page.
public boolean checkifemailexists(String email, DataSnapshot dataSnapshot){...}

Used to create a new user account in firebaseAuth section. Also calls the sendverification email method on successful signup. Used in Signup page.
public void registerNewEmail(final String firstname, final String lastname, final String email, String password){...}

Used to send a verification email to the user upon successful signup.
public void sendVerificationEmail(){...}

Add information to user_display and user_private nodes
public void addNewUser(String firstname, String lastname, String email){...}

Retrieves all profile info for current user. Stores the retrieved data as class called Userdataretrieval which has 2 attributes: users_private and users_display.
public Userdataretrieval getUserData(DataSnapshot dataSnapshot){...}

Retrieves an Arraylist of all users in the database and their profile information
public ArrayList<Userdataretrieval> getuserlist(DataSnapshot dataSnapshot){...}

Getter method for userID attribute
public String getUserID(){...}

Setter method for userID attribute
private void setUserID(String userID){...}
```



# **Future Works and Improvements**



1. The application could further be opened up to include other universities, and even businesses and startups.  \
This could create a community where there is both demand for projects and members and a constant healthy supply for talent that will promote an environment and community centered around learning and self improvement. 
2. Inclusion of more categories that would open doors to more types of projects that require very different skill sets. Since our application was initially targeted for a smaller target group of SUTD students, to reach out to other students would mean to include other skills that are in demand and less of an engineering nature. These could include Public Speaking, Adobe Suite, Google Suite, Writing, Literary Skills, Cooking, Baking, and the list goes on. This could be incorporated as the app’s reach expands, and can then be suitable even for graduates, adults, and even younger children.
3. Better suggestion algorithms based on the skill chips and “looking for” descriptions of each user and other matrics of user data.
4. Chat group creation so that the messages are consolidated. This can be done when a project lead receives many applications, and upon accepting applicants they are instantly added to a project chat that is created upon creation of the project. This makes it seamless for the project lead to communicate, and also not have to go to lengths to gather contact information and use another platform to create a communication channel. This would create better retention, and be more attractive as a project management tool.
5. A social feed so that one can see what others are also up to, what they have achieved etc. This would mimic Linkedin, as users would be able to follow/connect with other users and then see updates regarding their projects, especially for projects that they may be keen on being updated about but not be available to join. By attempting to serve as a social/communication/management tool, we hope to increase the versatility and attractiveness of our app by bringing together the best features of applications that are currently out there in a seamless way.


# **Libraries Used**



1. Firebase (Authentication and Real-Time Database)
2. Picasso ([https://square.github.io/picasso/](https://square.github.io/picasso/)) (For handling of image link loading)
3. Ismael Divita Chip Navigation Bar ([https://github.com/ismaeldivita/chip-navigation-bar](https://github.com/ismaeldivita/chip-navigation-bar))
4. De.hdodenhof Circle Image View ([https://github.com/hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView))


# **Team Contribution**

As our group had a unique mix of individuals with a multitude of different skill sets, tasks were distributed such that each group member was able to maximise their potential and work to their highest efficacy. The workload across all group members was relatively equal.


<table>
  <tr>
   <td><strong>Toh Kai Feng</strong>
   </td>
   <td>1004581
   </td>
   <td>
<ul>

<li>Implemented Final Profile UI 

<li>Implemented Uploading Pictures Function

<li>Implemented Firebase Start Up

<li>Implemented Project Application

<li>Implemented Project Management Function

<li>Database Management for Project Details

<li>Final Submission Poster
</li>
</ul>
   </td>
  </tr>
  <tr>
   <td><strong>Bryan Phengan Hengardi </strong>
   </td>
   <td>1004288
   </td>
   <td>
<ul>

<li>Implemented Profile Edit function

<li>Implemented Profile UI 

<li>Implemented Onboarding function and flow

<li>Implemented and debugged of profile setup and flow

<li>Implemented Uploading Pictures Function

<li>Database Management for User details

<li>Final Submission Video
</li>
</ul>
   </td>
  </tr>
  <tr>
   <td><strong>Seah Qi Yan</strong>
   </td>
   <td>1004628
   </td>
   <td>
<ul>

<li>Implemented nav bar and initialized fragments (Explore Projects)

<li>Implemented app’s project and chat search function 

<li>Implemented skills chip selection in profile set-up and editing

<li>Implemented project categories filter

<li>Implemented ‘Suggested for You’ personalised project recommendation

<li>Database Management for Project Details

<li>Final Submission Video
</li>
</ul>
   </td>
  </tr>
  <tr>
   <td><strong>Goh Shao Cong Shawn </strong>
   </td>
   <td>1004116
   </td>
   <td>
<ul>

<li>Implemented Chat Function UI and UX 

<li>Implemented Onboarding Functionality and Flow

<li>Implemented Sign In and Sign Up function

<li>Implemented Skeleton for entire project

<li>Database Management for User details and Chat details

<li>Final merging of all code fragments (Check for coherence in design)

<li>Final Submission Report

<li>Final Submission Poster
</li>
</ul>
   </td>
  </tr>
  <tr>
   <td><strong>Amrish Dev Sandhu</strong>
   </td>
   <td>1004241
   </td>
   <td>
<ul>

<li>Logo Concept and Creation

<li>Implemented Profile UI 

<li>Implemented Onboarding Design and UI

<li>Final Checkoff Slides and Presentation

<li>Final Submission Report
</li>
</ul>
   </td>
  </tr>
</table>


package com.hezd.contact.bean;

import android.annotation.SuppressLint;
import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 联系人
 */
public  class MyContact implements Serializable {
    private String key;
    private Long id;
    private String displayName;
    private String givenName;
    private String familyName;

    private List<String> phoneNumbers;
    private String photoUri;
    private final List<String> emails = new ArrayList<>();
    private String companyName;
    private String companyTitle;
    private final Set<String> websites = new HashSet<>();
    private List<String> addresses;
    private String note;

    interface AbstractField {
        String getMimeType();

        String getColumn();
    }

    public enum Field implements AbstractField {
        ContactId(null, ContactsContract.RawContacts.CONTACT_ID),
        DisplayName(null, ContactsContract.Data.DISPLAY_NAME),
        GivenName(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME),
        FamilyName(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME),
        PhoneNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER),
        PhoneType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE),
        PhoneLabel(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL),
        @SuppressLint("InlinedApi")
        PhoneNormalizedNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER),
        Email(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.ADDRESS),
        EmailType(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE),
        EmailLabel(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.LABEL),
        PhotoUri(null, ContactsContract.Data.PHOTO_URI),
        EventStartDate(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.START_DATE),
        EventType(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.TYPE),
        EventLabel(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.LABEL),
        CompanyName(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.COMPANY),
        CompanyTitle(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.TITLE),
        Website(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Website.URL),
        Note(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Note.NOTE),
        Address(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS),
        AddressType(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE),
        AddressStreet(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.STREET),
        AddressCity(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.CITY),
        AddressRegion(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.REGION),
        AddressPostcode(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE),
        AddressCountry(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY),
        AddressLabel(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.LABEL);

        private final String column;
        private final String mimeType;

        Field(String mimeType, String column) {
            this.mimeType = mimeType;
            this.column = column;
        }

        @Override
        public String getColumn() {
            return column;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    enum InternalField implements AbstractField {
        MimeType(null, ContactsContract.Data.MIMETYPE);

        private final String column;
        private final String mimeType;

        InternalField(String mimeType, String column) {
            this.mimeType = mimeType;
            this.column = column;
        }

        @Override
        public String getColumn() {
            return column;
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    public MyContact() {}

    void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets a the phone contact id.
     *
     * @return contact id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets a the display name the contact.
     *
     * @return Display Name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets a the given name the contact.
     *
     * @return Given Name.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Gets a the Family name the contact.
     *
     * @return Family Name.
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Gets a list of all phone numbers the contact has.
     *
     * @return A List of phone numbers.
     */
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Gets a contacts photo uri.
     *
     * @return Photo URI.
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Gets a list of all emails the contact has.
     *
     * @return A List of emails.
     */
    public List<String> getEmails() {
        return this.emails;
    }


    /**
     * Gets the name of the company the contact works on
     *
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Gets the job title of the contact
     *
     * @return the job title
     */
    public String getCompanyTitle() {
        return companyTitle;
    }
  
    /**
     * Gets the list of all websites the contact has
     *
     * @return A list of websites
     */
    public List<String> getWebsites() {
        return Arrays.asList(websites.toArray(new String[websites.size()]));
    }

    /**
     * Gets the note of the contact
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }
  
    /**
     * Gets the list of addresses
     *
     * @return A list of addresses
     */
    public List<String> getAddresses() {
        return addresses;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {

        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyContact contact = (MyContact) o;

        if (displayName != null ? !displayName.equals(contact.displayName) : contact.displayName != null)
            return false;
        return phoneNumbers != null ? phoneNumbers.equals(contact.phoneNumbers) : contact.phoneNumbers == null;
    }

    @Override
    public int hashCode() {
        int result = displayName != null ? displayName.hashCode() : 0;
        result = 31 * result + (phoneNumbers != null ? phoneNumbers.hashCode() : 0);
        return result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

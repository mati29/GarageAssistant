package main.java.GarageAssistantApp.StandardPackage;

/**
 * Created by Mati on 2017-03-24.
 */
public enum UserType {
        Admin("ROLE_ADMIN"),
        Employee("ROLE_WORKER"),
        Client("ROLE_USER");

        private final String text;

        private UserType(final String text) {
        this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
}

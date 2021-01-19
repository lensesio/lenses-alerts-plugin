## Lenses alert mail plugin

This is a Lenses plugin allowing to send Lenses raised alerts by email.

### Configuration

In order to enable the plugin, the following settings have to be set in Lenses:

```
    lenses.alerting.plugins = [
      {
        class=io.lenses.alerting.plugin.mail.MailAlertPlugin
        config={
           key1=value1
           key2=value2
           ...
        }
      }
    ]
```

Available configuration options:
 
 |Configuration    | Type   | Description                                                        |
 |-----------------|--------|--------------------------------------------------------------------|
 | from-address    | String | The sender mail address.                                           |
 | email-addresses | String | Comma separated address strings in RFC822 format.                  |
 | username        | String | The user name.                                                     |
 | password        | String | The password.                                                      |
 | smtp-host       | String | Host name of the SMTP mail server.                                 |
 | smtp-port       | String | Port of the SMTP mail server.                                      |
 | smtp-auth       | String | If true, attempt to authenticate the user using the AUTH command.  |
 | smtp-starttls   | String | If true, enables the use of the STARTTLS command.                  |
 

 
Google SMTP server example:

```
    lenses.alerting.plugins = [
      {
        class=io.lenses.alerting.plugin.mail.MailAlertPlugin
        config={
          from-address="sender@gmail.com"
          email-addresses="target1@mail.com, target2@mail.com"
          username="user"
          password="passwd"  // Generate application-specific password as described here: https://support.google.com/mail/?p=InvalidSecondFactor
          smtp-host="smtp.gmail.com"
          smtp-port="587"
          smtp-auth="true"
          smtp-starttls="true"
        }
      }
    ]
```


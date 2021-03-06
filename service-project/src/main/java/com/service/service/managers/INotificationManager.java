/*
 * Copyright 2013 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.service.service.managers;

import com.service.service.entity.Mailing;

import java.util.Collection;

/**
 * @author workhub
 */

public interface INotificationManager extends IManager {

	/**
	 * Returns true if the email service is configured and ready to send notifications.
	 *
	 * @return true if the email service is operational
	 * @since 1.6.0
	 */
	boolean isSendingMail();

	/**
	 * Notify the administrators by email.
	 *
	 * @param subject a
	 * @param message a
 	 * @since 1.4.0
	 */
	void sendMailToAdministrators(String subject, String message);

	/**
	 * Notify users by email of something.
	 *
	 * @param subject a
	 * @param message a
	 * @param toAddresses a
 	 * @since 1.4.0
	 */
	void sendMail(String subject, String message, Collection<String> toAddresses);

	/**
	 * Notify users by email of something.
	 *
	 * @param subject a
	 * @param message a
	 * @param toAddresses a
 	 * @since 1.4.0
	 */
	void sendHtmlMail(String subject, String message, Collection<String> toAddresses);

	/**
	 * Notify users by email of something.
	 *
	 * @param mailing a
	 * @return the mail message object
 	 * @since 1.4.0
	 */
	void send(Mailing mailing);

}
/*
 * Copyright 2014 gitblit.com.
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
package com.service.service.tickets;

import com.service.service.managers.*;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.TicketModel;
import com.service.service.entity.TicketModel.Attachment;
import com.service.service.entity.TicketModel.Change;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of a ticket service that rejects everything.
 *
 * @author James Moger
 *
 */
@Component
public class NullTicketService extends ITicketService {

	@Autowired
	public NullTicketService(
			IRuntimeManager runtimeManager,
			IPluginManager pluginManager,
			INotificationManager notificationManager,
			IUserManager userManager,
			IRepositoryManager repositoryManager) {

		super(runtimeManager,
				pluginManager,
				notificationManager,
				userManager,
				repositoryManager);
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void onStart() {
		log.info("{} started", getClass().getSimpleName());
	}

	@Override
	protected void resetCachesImpl() {
	}

	@Override
	protected void resetCachesImpl(TaskEntity repository) {
	}

	@Override
	protected void close() {
	}

	@Override
	public boolean hasTicket(TaskEntity repository, long ticketId) {
		return false;
	}

	@Override
	public synchronized Set<Long> getIds(TaskEntity repository) {
		return Collections.emptySet();
	}

	@Override
	public synchronized long assignNewId(TaskEntity repository) {
		return 0L;
	}

	@Override
	public List<TicketModel> getTickets(TaskEntity repository, TicketFilter filter) {
		return Collections.emptyList();
	}

	@Override
	protected TicketModel getTicketImpl(TaskEntity repository, long ticketId) {
		return null;
	}

	@Override
	protected List<Change> getJournalImpl(TaskEntity repository, long ticketId) {
		return null;
	}

	@Override
	public boolean supportsAttachments() {
		return false;
	}

	@Override
	public Attachment getAttachment(TaskEntity repository, long ticketId, String filename) {
		return null;
	}

	@Override
	protected synchronized boolean deleteTicketImpl(TaskEntity repository, TicketModel ticket, String deletedBy) {
		return false;
	}

	@Override
	protected synchronized boolean commitChangeImpl(TaskEntity repository, long ticketId, Change change) {
		return false;
	}

	@Override
	protected boolean deleteAllImpl(TaskEntity repository) {
		return false;
	}

	@Override
	protected boolean renameImpl(TaskEntity oldRepository, TaskEntity newRepository) {
		return false;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}

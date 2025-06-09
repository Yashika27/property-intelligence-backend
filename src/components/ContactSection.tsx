import React, { useState, useEffect } from 'react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Textarea } from './ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { User, Mail, Phone, FileText } from 'lucide-react';
import { toast } from 'sonner';

interface FormData {
  name: string;
  email: string;
  phone: string;
  service: string;
  message: string;
}

const ContactSection: React.FC = () => {
  const [service, setService] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formData, setFormData] = useState<FormData>({
    name: '',
    email: '',
    phone: '',
    service: '',
    message: ''
  });
  
  useEffect(() => {
    // Get the service from the URL if it exists
    const urlParams = new URLSearchParams(window.location.hash.split('?')[1]);
    const serviceParam = urlParams.get('service');
    if (serviceParam) {
      setService(serviceParam);
      setFormData(prev => ({ ...prev, service: serviceParam }));
    }
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { id, value } = e.target;
    setFormData(prev => ({ ...prev, [id]: value }));
  };

  const handleServiceChange = (value: string) => {
    setService(value);
    setFormData(prev => ({ ...prev, service: value }));
  };
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const response = await fetch('/submit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(formData),
      });

      if (response.ok) {
        toast.success('Message sent successfully!');
        // Reset form
        setFormData({
          name: '',
          email: '',
          phone: '',
          service: '',
          message: ''
        });
        setService('');
      } else {
        const errorText = await response.text();
        toast.error(`Failed to send message: ${errorText}`);
      }
    } catch (error) {
      toast.error('Failed to send message. Please try again later.');
      console.error('Error submitting form:', error);
    } finally {
      setIsSubmitting(false);
    }
  };
  
  return (
    <section className="py-12 px-6 bg-light-gray" id="contact-us">
      <div className="container mx-auto max-w-5xl">
        <h2 className="text-2xl font-bold text-center mb-10 text-navy">Contact Us</h2>
        
        <div className="bg-white rounded-lg shadow-lg overflow-hidden">
          <div className="flex flex-col md:flex-row">
            <div className="w-full md:w-1/2 bg-navy p-6 text-white">
              <h3 className="text-xl font-bold mb-5">Get In Touch</h3>
              <p className="mb-6 text-sm">
                Have questions about property investments or need legal advice? 
                Contact us today and our experts will be happy to assist you.
              </p>
              
              <div className="space-y-5">
                <div>
                  <h4 className="text-gold font-semibold mb-1 text-sm">Email</h4>
                  <p className="text-sm">info@propertyintelligence.com</p>
                </div>
                
                <div>
                  <h4 className="text-gold font-semibold mb-1 text-sm">Phone</h4>
                  <p className="text-sm">+1 (555) 123-4567</p>
                </div>
                
                <div>
                  <h4 className="text-gold font-semibold mb-1 text-sm">Follow Us</h4>
                  <p className="text-sm">Instagram: @property_intelligence</p>
                </div>
                
                <div className="mt-6 pt-5 border-t border-gray-700">
                  <p className="text-xs text-gray-300">
                    We usually respond to queries within 24 hours of receiving.
                  </p>
                </div>
              </div>
            </div>
            
            <div className="w-full md:w-1/2 p-6">
              <h3 className="text-xl font-bold mb-5 text-navy">Send a Message</h3>
              
              <form className="space-y-4" onSubmit={handleSubmit}>
                <div className="relative">
                  <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                    Full Name
                  </label>
                  <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <User className="h-4 w-4 text-gray-400" />
                    </div>
                    <Input 
                      id="name" 
                      placeholder="John Doe" 
                      className="pl-10 w-full text-sm"
                      value={formData.name}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>
                
                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                    Email Address
                  </label>
                  <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <Mail className="h-4 w-4 text-gray-400" />
                    </div>
                    <Input 
                      id="email" 
                      type="email" 
                      placeholder="john@example.com" 
                      className="pl-10 w-full text-sm"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>
                
                <div>
                  <label htmlFor="phone" className="block text-sm font-medium text-gray-700 mb-1">
                    Phone Number
                  </label>
                  <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <Phone className="h-4 w-4 text-gray-400" />
                    </div>
                    <Input 
                      id="phone" 
                      type="tel" 
                      placeholder="+1 (555) 123-4567" 
                      className="pl-10 w-full text-sm"
                      value={formData.phone}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>
                
                <div>
                  <label htmlFor="service" className="block text-sm font-medium text-gray-700 mb-1">
                    Service
                  </label>
                  <div className="relative">
                    <Select value={service} onValueChange={handleServiceChange}>
                      <SelectTrigger className="w-full text-sm">
                        <SelectValue placeholder="Select a service" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="property-evaluation">Property Evaluation</SelectItem>
                        <SelectItem value="legal-consultation">Legal Consultation</SelectItem>
                        <SelectItem value="documentation-review">Documentation Review</SelectItem>
                        <SelectItem value="risk-assessment">Risk Assessment</SelectItem>
                        <SelectItem value="other">Other</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
                
                <div>
                  <label htmlFor="message" className="block text-sm font-medium text-gray-700 mb-1">
                    Your Query
                  </label>
                  <div className="relative">
                    <div className="absolute top-2 left-3 pointer-events-none">
                      <FileText className="h-4 w-4 text-gray-400" />
                    </div>
                    <Textarea 
                      id="message" 
                      placeholder="How can we help you?" 
                      className="pl-10 w-full min-h-[100px] pt-2 text-sm"
                      value={formData.message}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>
                
                <Button 
                  type="submit" 
                  className="w-full bg-gold hover:bg-gold/80 text-navy font-medium text-sm"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? 'Sending...' : 'Send Message'}
                </Button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default ContactSection; 